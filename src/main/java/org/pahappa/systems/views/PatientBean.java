package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import org.pahappa.systems.enums.PatientType;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.navigation.NavigationBean;
import org.pahappa.systems.services.medical.Impl.MedicalReportServiceImpl;
import org.pahappa.systems.services.patient.PatientService;
import jakarta.inject.Inject;
import org.pahappa.systems.enums.Gender;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.LocalDate;
import org.pahappa.systems.models.MedicalReport;

import java.util.Collections;

@Named("patientBean")
@SessionScoped // Use SessionScoped for reliable state across dialogs and AJAX
public class PatientBean implements Serializable {

    // Data lists for the view
    private List<Patient> allPatients;
    private List<Patient> filteredPatients; // For global search
    private List<Patient> hospitalizedPatients;
    private List<Patient> outpatients;

    private String globalFilter; // For the search input

    private Patient newPatient = new Patient();
    private String message;

    private Patient selectedPatient = new Patient();
    private Patient patientToDelete;

    @Inject
    private PatientService patientService;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private MedicalReportServiceImpl medicalReportService;

    private List<MedicalReport> selectedPatientReports = Collections.emptyList();
    private MedicalReport currentMedicalReport;

    @PostConstruct
    public void init() {
        // Use the injected patientService, do not create a new one here
        // Load all patient data once
        allPatients = patientService.getAllPatients().stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
        // Pre-filter the lists for the tabs
        hospitalizedPatients = allPatients.stream()
                .filter(p -> p.getPatientType() == PatientType.INPATIENT)
                .collect(Collectors.toList());
        outpatients = allPatients.stream()
                .filter(p -> p.getPatientType() == PatientType.OUTPATIENT)
                .collect(Collectors.toList());
        // Initially, the filtered list is the same as the full list
        filteredPatients = allPatients;
    }

    // --- Action Methods ---

//    public void filterPatients() {
//        if (globalFilter == null || globalFilter.trim().isEmpty()) {
//            filteredPatients = allPatients; // If search is empty, show all
//        } else {
//            String lowerCaseFilter = globalFilter.toLowerCase();
//            filteredPatients = allPatients.stream()
//                    .filter(p -> p.getFullName().toLowerCase().contains(lowerCaseFilter) ||
//                            (p.getEmail() != null && p.getEmail().toLowerCase().contains(lowerCaseFilter)) ||
//                            (p.getInsuranceNumber() != null && p.getInsuranceNumber().contains(lowerCaseFilter)))
//                    .collect(Collectors.toList());
//        }
//    }

    public String savePatient() {
        try {
            // Create a Patient from the current newPatient (as User)
            Patient patientToSave = new Patient(newPatient);
            patientToSave.setInsuranceNumber(newPatient.getInsuranceNumber());
            patientToSave.setPatientType(newPatient.getPatientType());
            patientService.savePatient(patientToSave);
            // Refresh lists using service method
            Map<String, List<Patient>> lists = patientService.getPatientLists();
            allPatients = lists.get("all").stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
            hospitalizedPatients = lists.get("hospitalized").stream().filter(p -> !p.isDeleted())
                    .collect(Collectors.toList());
            outpatients = lists.get("outpatients").stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
            filteredPatients = allPatients;
            message = "Patient added successfully!";
            newPatient = new Patient();
            return "/patients.xhtml?faces-redirect=true";
        } catch (Exception e) {
            message = "Error adding patient: " + e.getMessage();
            return null;
        }
    }

    // Called when Edit button is pressed, sets selected patient and navigates to
    // edit page
    public String editPatient(Patient patient) {
        this.selectedPatient = patient;
        return navigationBean.toEditPatient();
    }

    // Called from edit-patient.xhtml to update patient info
    public String updatePatient() {
        try {
            patientService.updatePatient(selectedPatient);
            // Refresh lists
            Map<String, List<Patient>> lists = patientService.getPatientLists();
            allPatients = lists.get("all").stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
            hospitalizedPatients = lists.get("hospitalized").stream().filter(p -> !p.isDeleted())
                    .collect(Collectors.toList());
            outpatients = lists.get("outpatients").stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
            filteredPatients = allPatients;
            message = "Patient updated successfully!";
            return navigationBean.toPatients();
        } catch (Exception e) {
            message = "Error updating patient: " + e.getMessage();
            return null;
        }
    }

    // Called when Delete button is pressed, sets patient to delete and shows modal
    public void confirmDelete(Patient patient) {
        this.patientToDelete = patient;
    }

    // Called when delete is confirmed in modal
    public void deletePatient() {
        try {
            patientToDelete.setDeleted(true);
            patientService.updatePatient(patientToDelete); // Soft

            
            // Refresh lists and filter out deleted patients
            Map<String, List<Patient>> lists = patientService.getPatientLists();
            allPatients = lists.get("all").stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
            hospitalizedPatients = lists.get("hospitalized").stream().filter(p -> !p.isDeleted())
                    .collect(Collectors.toList());
            outpatients = lists.get("outpatients").stream().filter(p -> !p.isDeleted()).collect(Collectors.toList());
            filteredPatients = allPatients;
            message = "Patient deleted successfully!";
        } catch (Exception e) {
            message = "Error deleting patient: " + e.getMessage();
        }
    }

    // Call this when the medical form icon is clicked
    public void loadMedicalReportsForPatient(Patient patient) {
        this.selectedPatient = patient;
        this.selectedPatientReports = medicalReportService.getReportsByPatient(patient);
    }

    public List<MedicalReport> getSelectedPatientReports() {
        return selectedPatientReports;
    }

    public MedicalReport getCurrentMedicalReport() {
        return currentMedicalReport;
    }

    public void setCurrentMedicalReport(MedicalReport report) {
        this.currentMedicalReport = report;
    }

    // --- Getters and Setters ---

    public List<Patient> getAllPatients() {
        return allPatients;
    }

    public List<Patient> getFilteredPatients() {
        return filteredPatients;
    }

    public List<Patient> getHospitalizedPatients() {
        return hospitalizedPatients;
    }

    public List<Patient> getOutpatients() {
        return outpatients;
    }

    public String getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    public Patient getNewPatient() {
        return newPatient;
    }

    public void setNewPatient(Patient newPatient) {
        this.newPatient = newPatient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PatientType[] getPatientTypes() {
        return PatientType.values();
    }

    public Gender[] getGenders() {
        return Gender.values();
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public Patient getPatientToDelete() {
        return patientToDelete;
    }

    public void setPatientToDelete(Patient patientToDelete) {
        this.patientToDelete = patientToDelete;
    }

    public LocalDate getNow() {
        return java.time.LocalDate.now();
    }

}

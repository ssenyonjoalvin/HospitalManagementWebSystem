package org.pahappa.systems.beans;

import jakarta.annotation.PostConstruct;
import jakarta.el.MethodExpression;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.pahappa.systems.enums.PatientType;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.services.PatientService;
import jakarta.inject.Inject;
import org.pahappa.systems.enums.Gender;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

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

    @PostConstruct
    public void init() {
        // Use the injected patientService, do not create a new one here
        // Load all patient data once
        allPatients = patientService.getAllPatients();
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

    public void filterPatients() {
        if (globalFilter == null || globalFilter.trim().isEmpty()) {
            filteredPatients = allPatients; // If search is empty, show all
        } else {
            String lowerCaseFilter = globalFilter.toLowerCase();
            filteredPatients = allPatients.stream()
                    .filter(p -> p.getFullName().toLowerCase().contains(lowerCaseFilter) ||
                            (p.getEmail() != null && p.getEmail().toLowerCase().contains(lowerCaseFilter)) ||
                            (p.getInsuranceNumber() != null && p.getInsuranceNumber().contains(lowerCaseFilter)))
                    .collect(Collectors.toList());
        }
    }

    public String savePatient() {
        try {
            // Create a Patient from the current newPatient (as User)
            Patient patientToSave = new Patient(newPatient);
            patientToSave.setInsuranceNumber(newPatient.getInsuranceNumber());
            patientToSave.setPatientType(newPatient.getPatientType());
            patientService.savePatient(patientToSave);
            // Refresh lists using service method
            Map<String, List<Patient>> lists = patientService.getPatientLists();
            allPatients = lists.get("all");
            hospitalizedPatients = lists.get("hospitalized");
            outpatients = lists.get("outpatients");
            filteredPatients = allPatients;
            message = "Patient added successfully!";
            return navigationBean.toPatients();
            // newPatient = new Patient();
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
            allPatients = lists.get("all");
            hospitalizedPatients = lists.get("hospitalized");
            outpatients = lists.get("outpatients");
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
            patientService.deletePatient(patientToDelete);
            // Refresh lists
            Map<String, List<Patient>> lists = patientService.getPatientLists();
            allPatients = lists.get("all");
            hospitalizedPatients = lists.get("hospitalized");
            outpatients = lists.get("outpatients");
            filteredPatients = allPatients;
            message = "Patient deleted successfully!";
        } catch (Exception e) {
            message = "Error deleting patient: " + e.getMessage();
        }
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


}

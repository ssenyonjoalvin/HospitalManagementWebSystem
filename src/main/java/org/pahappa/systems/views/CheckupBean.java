package org.pahappa.systems.views;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.Appointment;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.models.Medicine;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.models.Service;
import org.pahappa.systems.repository.AppointmentsDAO;
import org.pahappa.systems.services.billing.BillingAndReportingService;
import org.pahappa.systems.services.session.SessionManager;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named("checkupBean")
@ViewScoped
public class CheckupBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private BillingAndReportingService billingService;

    @Inject
    private SessionManager sessionManager;

    // Form data
    private Patient selectedPatient;
    private String signsAndSymptoms;
    private String conclusion;
    private String diagnosis;
    private String treatmentPlan;

    // Selected items
    private List<Medicine> selectedMedicines = new ArrayList<>();
    private List<Service> selectedServices = new ArrayList<>();

    // Available options
    private List<Medicine> availableMedicines;
    private List<Service> availableServices;

    // Dialog state
    private boolean dialogVisible = false;

    private Appointment selectedAppointment;
    private Doctor selectedDoctor;

    private Long selectedAppointmentId;

    public String goToCheckupPage(Appointment appointment) {
        return "/checkup.xhtml?faces-redirect=true&amp;appointmentId=" + appointment.getId();
    }

    @Deprecated
    public void prepareCheckup(Appointment appointment) {
        // Deprecated: use goToCheckupPage for navigation
        this.selectedAppointment = appointment;
        this.selectedPatient = appointment.getPatient();
        this.selectedDoctor = appointment.getDoctor();
        this.signsAndSymptoms = "";
        this.conclusion = "";
        this.diagnosis = "";
        this.treatmentPlan = "";
        this.selectedMedicines.clear();
        this.selectedServices.clear();
        this.availableMedicines = billingService.getAllMedicines();
        this.availableServices = billingService.getAllServices();
        this.dialogVisible = true;
    }

    public void submitCheckup() {
        System.out.println("submitCheckup called"); // Debug print
        try {
            Long doctorId = selectedDoctor != null ? selectedDoctor.getId() : sessionManager.getCurrentUser().getId();
            List<Long> medicineIds = selectedMedicines.stream().map(Medicine::getId).toList();
            List<Long> serviceIds = selectedServices.stream().map(Service::getId).toList();
            BillingAndReportingService.CheckupResult result = billingService.processCheckup(
                    selectedPatient.getId(),
                    doctorId,
                    signsAndSymptoms,
                    conclusion,
                    medicineIds,
                    serviceIds);
            if (result.isSuccess()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Checkup Completed",
                                "Medical report and invoice created successfully. " +
                                        "Report ID: " + result.getMedicalReportId() +
                                        ", Invoice ID: " + result.getInvoiceId()));
                closeDialog();
                PrimeFaces.current().ajax().update("checkupForm:growlMsg");
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Checkup Failed",
                                result.getMessage()));
                PrimeFaces.current().ajax().update("checkupForm:growlMsg");
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "System Error",
                            "An unexpected error occurred: " + e.getMessage()));
            PrimeFaces.current().ajax().update("checkupForm:growlMsg");
        }
    }

    public void closeDialog() {
        this.dialogVisible = false;
        this.selectedPatient = null;
        this.signsAndSymptoms = "";
        this.conclusion = "";
        this.diagnosis = "";
        this.treatmentPlan = "";
        this.selectedMedicines.clear();
        this.selectedServices.clear();
    }

    public void cancelCheckup() {
        closeDialog();
    }

    // Getters and Setters
    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public String getSignsAndSymptoms() {
        return signsAndSymptoms;
    }

    public void setSignsAndSymptoms(String signsAndSymptoms) {
        this.signsAndSymptoms = signsAndSymptoms;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public List<Medicine> getSelectedMedicines() {
        return selectedMedicines;
    }

    public void setSelectedMedicines(List<Medicine> selectedMedicines) {
        this.selectedMedicines = selectedMedicines;
    }

    public List<Service> getSelectedServices() {
        return selectedServices;
    }

    public void setSelectedServices(List<Service> selectedServices) {
        this.selectedServices = selectedServices;
    }

    public List<Medicine> getAvailableMedicines() {
        return availableMedicines;
    }

    public void setAvailableMedicines(List<Medicine> availableMedicines) {
        this.availableMedicines = availableMedicines;
    }

    public List<Service> getAvailableServices() {
        return availableServices;
    }

    public void setAvailableServices(List<Service> availableServices) {
        this.availableServices = availableServices;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }

    public Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(Doctor selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public Long getSelectedAppointmentId() {
        return selectedAppointmentId;
    }

    public void setSelectedAppointmentId(Long id) {
        this.selectedAppointmentId = id;
    }

    public void loadAppointmentData() {
        if (selectedAppointmentId != null
                && (selectedAppointment == null || !selectedAppointment.getId().equals(selectedAppointmentId))) {
            // Load appointment from DB using selectedAppointmentId
            // You may need to inject AppointmentsDAO or similar here
            AppointmentsDAO appointmentsDAO = new AppointmentsDAO(); // Or inject if possible
            this.selectedAppointment = appointmentsDAO.findById(selectedAppointmentId);
            if (this.selectedAppointment != null) {
                this.selectedPatient = selectedAppointment.getPatient();
                this.selectedDoctor = selectedAppointment.getDoctor();
                this.signsAndSymptoms = "";
                this.conclusion = "";
                this.diagnosis = "";
                this.treatmentPlan = "";
                this.selectedMedicines = new ArrayList<>();
                this.selectedServices = new ArrayList<>();
                this.availableMedicines = billingService.getAllMedicines();
                this.availableServices = billingService.getAllServices();
            }
        }
    }

    public List<Medicine> completeMedicine(String query) {
        if (availableMedicines == null) {
            availableMedicines = billingService.getAllMedicines();
        }
        String lowerQuery = query == null ? "" : query.toLowerCase();
        return availableMedicines.stream()
                .filter(med -> med.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<Service> completeService(String query) {
        if (availableServices == null) {
            availableServices = billingService.getAllServices();
        }
        String lowerQuery = query == null ? "" : query.toLowerCase();
        return availableServices.stream()
                .filter(svc -> svc.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}
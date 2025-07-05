package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.core.services.exceptions.ValidationException;
import org.pahappa.systems.enums.AppointmentStatus;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Specialty;
import org.pahappa.systems.enums.TimeSlot;
import org.pahappa.systems.models.Appointment;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.navigation.NavigationBean;
import org.pahappa.systems.repository.UserDAO;
import org.pahappa.systems.services.appointment.AppointmentsService;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A SessionScoped bean to manage the state for both the appointments list
 * and the creation/editing of a single appointment across multiple pages.
 */
@Named("appointmentBean")
@SessionScoped
public class AppointmentBean implements Serializable {

    // --- Injected Dependencies ---
    // The bean only knows about services and other beans, not DAOs.
    @Inject
    private AppointmentsService appointmentsService;
    @Inject
    private UserDAO userDAO; // Used only to get the initial list of patients
    @Inject
    private NavigationBean navigationBean;

    // --- Data for the Main Appointments List Page ---
    private List<Appointment> appointments;

    // --- State for the New/Edit Appointment Page ---
    private Appointment appointmentToCreate; // The object bound to the 'new-appointment' form
    private Patient selectedPatient;
    private Specialty selectedSpecialty;
    private List<Doctor> availableDoctors; // For the cascading dropdown
    private Doctor selectedDoctor;

    // --- Shared Data for Dropdowns ---
    private List<Patient> allPatients;
    private final List<Specialty> allSpecialties = Arrays.asList(Specialty.values());
    private final List<TimeSlot> allTimeSlots = Arrays.asList(TimeSlot.values());

    // --- Properties used in appointments.xhtml and new-appointment.xhtml ---
    private List<Doctor> doctors;
    private List<AppointmentStatus> statuses = Arrays.asList(AppointmentStatus.values());
    private AppointmentStatus selectedStatus;
    private List<Patient> patients;
    private List<Specialty> specialties;
    private List<TimeSlot> availableTimeSlots;
    private Appointment newAppointment;
    private LocalDate selectedDate;
    private TimeSlot selectedTimeSlot;
    private String message;

    // --- Cancel with Reason Dialog State ---
    private Appointment appointmentToCancel;
    private String cancelReason;

    // --- Edit Appointment State ---
    private Appointment appointmentToEdit;

    /**
     * The PostConstruct method is called once after the bean is created.
     * It loads the data needed for the main appointments list.
     */
    @PostConstruct
    public void init() {
        // Load the main list of appointments
        this.appointments = appointmentsService.getAllAppointments();

        // Load the list of all patients once for the autocomplete feature
        this.allPatients = userDAO.getAllRecords().stream()
                .filter(user -> user.getRole() == Rolename.PATIENT && user instanceof Patient)
                .map(user -> (Patient) user)
                .collect(Collectors.toList());

        if (newAppointment == null) {
            newAppointment = new Appointment();
        }
    }

    // --- ACTION METHODS (Triggered by user clicks) ---

    /**
     * Prepares the bean for navigating to the new appointment page.
     * This method is called when the "New Appointment" button is clicked.
     * 
     * @return The navigation outcome to redirect to the new appointment page.
     */
    public String goToNewAppointment() {
        // 1. Create a fresh, empty Appointment object for the form
        this.appointmentToCreate = new Appointment();
        this.appointmentToCreate.setAppointmentDate(LocalDate.now()); // Default to today

        // 2. Reset all UI selections for a clean form
        this.selectedPatient = null;
        this.selectedSpecialty = null;
        this.availableDoctors = Collections.emptyList();

        // 3. Delegate navigation to the NavigationBean
        return navigationBean.toNewAppointment();
    }

    /**
     * Saves the new appointment. This method is called from the
     * 'new-appointment.xhtml' page.
     * It handles validation and gives user feedback.
     */
    public String saveNewAppointment() {
        System.out.println("[DEBUG] saveNewAppointment called. selectedSpecialty: " + selectedSpecialty
                + ", availableDoctors: " + availableDoctors);
        try {
            if ((availableDoctors == null || availableDoctors.isEmpty()) && selectedSpecialty != null) {
                availableDoctors = appointmentsService.getDoctorsBySpecialty(selectedSpecialty);
                System.out.println("[DEBUG] Repopulated availableDoctors in saveNewAppointment: " + availableDoctors);
            }
            // 1. Final validation and state assembly before calling the service
            if (selectedPatient == null) {
                throw new ValidationException("A patient must be selected for the appointment.");
            }
            appointmentToCreate.setPatient(selectedPatient);
            System.out.println("[DEBUG] About to save appointment. Patient: " + selectedPatient + ", Patient ID: "
                    + selectedPatient.getId());
            // 2. Delegate ALL business logic and persistence to the service layer
            appointmentsService.createAppointmentFromObject(appointmentToCreate);
            // 3. On success, provide feedback to the user
            addMessage(FacesMessage.SEVERITY_INFO, "Success",
                    "Appointment for " + selectedPatient.getFullName() + " has been scheduled.");
            // 4. Refresh the appointments list so the new appointment appears
            this.appointments = appointmentsService.getAllAppointments();
            // 5. Redirect to the appointments page
            return navigationBean.toAppointments();
        } catch (ValidationException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", e.getMessage());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_FATAL, "System Error",
                    "An unexpected error occurred. Please contact support.");
            return null;
        }
    }

    // --- AJAX Listener Methods ---

    /**
     * Called via AJAX when the user selects a specialty from the dropdown.
     * It populates the list of available doctors for that specialty.
     */
    public void onSpecialtyChange() {
        if (selectedSpecialty != null) {
            this.availableDoctors = appointmentsService.getDoctorsBySpecialty(selectedSpecialty);
        } else {
            this.availableDoctors = Collections.emptyList();
        }
        // Clear the doctor selection when the specialty changes
        if (appointmentToCreate != null) {
            appointmentToCreate.setDoctor(null);
        }
    }

    // --- Autocomplete Method ---

    /**
     * Provides a filtered list of patients for the p:autoComplete component.
     * 
     * @param query The text typed by the user.
     * @return A list of matching patients.
     */
    public List<Patient> searchPatients(String query) {
        String lowerCaseQuery = query == null ? "" : query.toLowerCase();
        return allPatients.stream()
                .filter(p -> p.getFullName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    // --- Helper Methods ---

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public LocalDate getToday() {
        return LocalDate.now();
    }

    // --- GETTERS AND SETTERS for all properties used by the view ---

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Appointment getAppointmentToCreate() {
        return appointmentToCreate;
    }

    public void setAppointmentToCreate(Appointment appointmentToCreate) {
        this.appointmentToCreate = appointmentToCreate;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public Specialty getSelectedSpecialty() {
        return selectedSpecialty;
    }

    public void setSelectedSpecialty(Specialty selectedSpecialty) {
        this.selectedSpecialty = selectedSpecialty;
    }

    public List<Doctor> getAvailableDoctors() {
        if ((availableDoctors == null || availableDoctors.isEmpty()) && selectedSpecialty != null) {
            availableDoctors = appointmentsService.getDoctorsBySpecialty(selectedSpecialty);
        }
        System.out.println("[DEBUG] getAvailableDoctors called. selectedSpecialty: " + selectedSpecialty
                + ", availableDoctors: " + availableDoctors);
        return availableDoctors;
    }

    public void setAvailableDoctors(List<Doctor> availableDoctors) {
        this.availableDoctors = availableDoctors;
    }

    public List<Patient> getAllPatients() {
        return allPatients;
    }

    public List<Specialty> getAllSpecialties() {
        return allSpecialties;
    }

    public List<TimeSlot> getAllTimeSlots() {
        return Arrays.asList(TimeSlot.values());
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    public void setSelectedDoctor(Doctor selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<AppointmentStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<AppointmentStatus> statuses) {
        this.statuses = statuses;
    }

    public AppointmentStatus getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(AppointmentStatus selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }

    public List<TimeSlot> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<TimeSlot> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public Appointment getNewAppointment() {
        return newAppointment;
    }

    public void setNewAppointment(Appointment newAppointment) {
        this.newAppointment = newAppointment;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public TimeSlot getSelectedTimeSlot() {
        return selectedTimeSlot;
    }

    public void setSelectedTimeSlot(TimeSlot selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // --- Cancel with Reason Methods ---
    public void openCancelDialog(Appointment appt) {
        this.appointmentToCancel = appt;
        this.cancelReason = null;
    }

    public void confirmCancel() {
        if (appointmentToCancel != null && cancelReason != null && !cancelReason.trim().isEmpty()) {
            try {
                // Set the new values
                appointmentToCancel.setReason(cancelReason);
                appointmentToCancel.setStatus(AppointmentStatus.CANCELED);

                // Persist the changes to the database
                appointmentsService.updateAppointment(appointmentToCancel);

                // Feedback and refresh
                addMessage(FacesMessage.SEVERITY_INFO, "Appointment canceled", "Reason: " + cancelReason);
                this.appointments = appointmentsService.getAllAppointments();
            } catch (Exception e) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not cancel appointment: " + e.getMessage());
            }
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Reason required", "Please enter a reason for cancellation.");
        }
    }

    public Appointment getAppointmentToCancel() {
        return appointmentToCancel;
    }

    public void setAppointmentToCancel(Appointment appointmentToCancel) {
        this.appointmentToCancel = appointmentToCancel;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String edit(Appointment appt) {
        this.appointmentToEdit = appt;
        this.appointmentToCreate = appt; // reuse the same object for the form
        this.selectedPatient = appt.getPatient();
        this.selectedSpecialty = appt.getDoctor() != null ? appt.getDoctor().getSpecialization() : null;
        this.availableDoctors = appointmentsService.getDoctorsBySpecialty(selectedSpecialty);
        return navigationBean.toNewAppointment();
    }

    public Appointment getAppointmentToEdit() {
        return appointmentToEdit;
    }

    public void setAppointmentToEdit(Appointment appointmentToEdit) {
        this.appointmentToEdit = appointmentToEdit;
    }
}
package org.pahappa.systems.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.pahappa.systems.enums.AppointmentStatus;
import org.pahappa.systems.enums.TimeSlot;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "rescheduled_date")
    private LocalDate rescheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot", nullable = false)
    private TimeSlot timeSlot;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // A single, clear status field
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    // The relationship to a patient. Can be null initially.
    @ManyToOne(optional = true) // This is the missing annotation
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "reason")
    private String reason;

    // --- Getters and Setters ---
    // (The redundant fields and their getters/setters have been removed)

    public LocalDate getRescheduledDate() {
        return rescheduledDate;
    }

    public void setRescheduledDate(LocalDate rescheduledDate) {
        this.rescheduledDate = rescheduledDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
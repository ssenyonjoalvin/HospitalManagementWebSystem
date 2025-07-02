package org.pahappa.systems.services;

import org.pahappa.systems.core.services.exceptions.ValidationException;
import org.pahappa.systems.enums.AppointmentStatus;
import org.pahappa.systems.enums.TimeSlot;
import org.pahappa.systems.models.Appointment;
import org.pahappa.systems.models.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentsService {

    List<Appointment> getAllAppointments();

    /**
     * Creates a new appointment after validating the inputs.
     * 
     * @return The newly created Appointment.
     * @throws ValidationException if the doctor, date, or timeslot is invalid, or
     *                             if the slot is already taken.
     */
    Appointment createAppointment(long doctorId, LocalDate date, TimeSlot timeSlot) throws ValidationException;

    /**
     * Cancels an existing appointment.
     * 
     * @param appointmentId The ID of the appointment to cancel.
     * @throws ValidationException if the appointment cannot be found or is already
     *                             completed/canceled.
     */
    void cancelAppointment(long appointmentId) throws ValidationException;

    /**
     * Reschedules an existing appointment to a new date and time.
     * 
     * @param appointmentId The ID of the appointment to reschedule.
     * @param newDate       The new date for the appointment.
     * @param newTimeSlot   The new timeslot for the appointment.
     * @return The updated Appointment object.
     * @throws ValidationException if the appointment cannot be found, or if the new
     *                             slot is taken.
     */
    Appointment rescheduleAppointment(long appointmentId, LocalDate newDate, TimeSlot newTimeSlot)
            throws ValidationException;

    List<Doctor> getDoctorsBySpecialty(org.pahappa.systems.enums.Specialty specialty);

    List<org.pahappa.systems.enums.TimeSlot> getAvailableTimeSlots(long doctorId, java.time.LocalDate date)
            throws ValidationException;

    void createAppointmentFromObject(Appointment appointment) throws ValidationException;
}
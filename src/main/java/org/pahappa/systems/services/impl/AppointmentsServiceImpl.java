package org.pahappa.systems.services.impl;

import org.pahappa.systems.core.services.exceptions.ValidationException;
import org.pahappa.systems.enums.AppointmentStatus;
import org.pahappa.systems.enums.TimeSlot;
import org.pahappa.systems.enums.Specialty;
import org.pahappa.systems.models.Appointment;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.repository.AppointmentsDAO;
import org.pahappa.systems.repository.UserDAO; // Assuming you have a DoctorDAO
import org.pahappa.systems.services.AppointmentsService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

@ApplicationScoped
@Named
public class AppointmentsServiceImpl implements AppointmentsService {

    private final AppointmentsDAO appointmentsDAO = new AppointmentsDAO();
    private final UserDAO userDAO = new UserDAO(); // You will need this

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentsDAO.findAll();
    }

    @Override
    public Appointment createAppointment(long doctorId, LocalDate date, TimeSlot timeSlot) throws ValidationException {
        // 1. Validate Inputs
        if (date == null || timeSlot == null || doctorId <= 0) {
            throw new ValidationException("Doctor, date, and time slot must be provided.");
        }
        Doctor doctor = userDAO.findById(doctorId);
        if (doctor == null) {
            throw new ValidationException("Selected doctor not found.");
        }

        // 2. Check Business Rule: Is the slot already taken?
        if (appointmentsDAO.isSlotTaken(doctor, date, timeSlot)) {
            throw new ValidationException(
                    "This time slot is already booked for the selected doctor on " + date + ". Please choose another.");
        }

        // 3. Create the Appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus(AppointmentStatus.SCHEDULED); // Default status

        // 4. Persist
        appointmentsDAO.save(appointment);
        return appointment;
    }

    @Override
    public void cancelAppointment(long appointmentId) throws ValidationException {
        Appointment appointment = appointmentsDAO.findById(appointmentId);
        if (appointment == null) {
            throw new ValidationException("Appointment not found.");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED
                || appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new ValidationException("Cannot cancel an appointment that is already completed or canceled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentsDAO.update(appointment);
    }

    @Override
    public Appointment rescheduleAppointment(long appointmentId, LocalDate newDate, TimeSlot newTimeSlot)
            throws ValidationException {
        // 1. Validate Inputs
        if (newDate == null || newTimeSlot == null) {
            throw new ValidationException("A new date and time slot must be provided for rescheduling.");
        }
        Appointment appointment = appointmentsDAO.findById(appointmentId);
        if (appointment == null) {
            throw new ValidationException("Appointment not found.");
        }

        // 2. Check Business Rule: Is the new slot taken?
        List<Appointment> appointmentsInSlot = appointmentsDAO.findByDoctorAndDate(appointment.getDoctor(), newDate);
        if (appointmentsInSlot.stream().anyMatch(a -> a.getTimeSlot() == newTimeSlot && a.getId() != appointmentId)) {
            throw new ValidationException("The new time slot is already booked. Please choose another.");
        }

        // 3. Update the Appointment
        appointment.setAppointmentDate(newDate);
        appointment.setTimeSlot(newTimeSlot);
        appointment.setStatus(AppointmentStatus.SCHEDULED); // Reset status to scheduled

        // 4. Persist
        appointmentsDAO.update(appointment);
        return appointment;
    }

    @Override
    public List<Doctor> getDoctorsBySpecialty(Specialty specialty) {
        return userDAO.findDoctorsBySpecialty(specialty);
    }

    @Override
    public List<TimeSlot> getAvailableTimeSlots(long doctorId, LocalDate date) throws ValidationException {
        Doctor doctor = userDAO.findById(doctorId);
        if (doctor == null) {
            throw new ValidationException("Doctor not found.");
        }
        List<TimeSlot> allSlots = Arrays.asList(TimeSlot.values());
        List<Appointment> appointments = appointmentsDAO.findByDoctorAndDate(doctor, date);
        Set<TimeSlot> takenSlots = appointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toSet());
        return allSlots.stream()
                .filter(slot -> !takenSlots.contains(slot))
                .collect(Collectors.toList());
    }

    @Override
    public void createAppointmentFromObject(Appointment appointment) throws ValidationException {
        // Validate the incoming object
        if (appointment == null || appointment.getDoctor() == null || appointment.getAppointmentDate() == null || appointment.getTimeSlot() == null) {
            throw new ValidationException("Doctor, date, and timeslot are all required.");
        }

        // Call your existing business logic
        this.createAppointment(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                appointment.getTimeSlot()
        );
        // Note: The original createAppointment already handles saving the patient now, so no need to save again.
    }
}
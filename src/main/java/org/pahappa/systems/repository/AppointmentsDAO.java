package org.pahappa.systems.repository; // Or your preferred package

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.pahappa.systems.models.Appointment;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.enums.TimeSlot;
import org.pahappa.systems.util.HibernateUtil; // Assuming you have this utility

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AppointmentsDAO {

    /**
     * Saves a new appointment to the database.
     */
    public void save(Appointment appointment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(appointment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing appointment.
     */
    public void update(Appointment appointment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(appointment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all appointments from the database, ordered by id descending (most
     * recent first).
     */
    public List<Appointment> findAll() {
        System.out.println("[DEBUG] AppointmentsDAO.findAll() called");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Appointment> appointments = session
                    .createQuery("from Appointment a order by a.id desc", Appointment.class).list();
            System.out.println("[DEBUG] Found " + appointments.size() + " appointments in database");
            for (Appointment appt : appointments) {
                System.out.println("[DEBUG] Appointment: ID=" + appt.getId() +
                        ", Patient=" + (appt.getPatient() != null ? appt.getPatient().getFullName() : "null") +
                        ", Doctor=" + (appt.getDoctor() != null ? appt.getDoctor().getFullName() : "null") +
                        ", Date=" + appt.getAppointmentDate() + ", Status=" + appt.getStatus());
            }
            return appointments;
        } catch (Exception e) {
            System.err.println("[ERROR] Error in AppointmentsDAO.findAll(): " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list on error
        }
    }

    /**
     * Finds an appointment by its ID.
     */
    public Appointment findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Appointment.class, id);
        }
    }

    /**
     * Deletes an appointment by its ID.
     */
    public void delete(long id) {
        Transaction transaction = null;
        Appointment appointment = findById(id);
        if (appointment == null) {
            return; // Nothing to delete
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(appointment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Checks if a specific time slot is already booked for a given doctor on a
     * given date.
     */
    public boolean isSlotTaken(Doctor doctor, LocalDate date, TimeSlot timeSlot) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(a) FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDate = :date AND a.timeSlot = :slot",
                    Long.class);

            query.setParameter("doctor", doctor);
            query.setParameter("date", date);
            query.setParameter("slot", timeSlot);

            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            // In case of error, it's safer to assume the slot is taken to prevent double
            // booking.
            return true;
        }
    }

    /**
     * Finds all appointments for a given doctor and date.
     */
    public List<Appointment> findByDoctorAndDate(Doctor doctor, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDate = :date",
                    Appointment.class)
                    .setParameter("doctor", doctor)
                    .setParameter("date", date)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
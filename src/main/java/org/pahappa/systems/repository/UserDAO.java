package org.pahappa.systems.repository;

import org.pahappa.systems.enums.Specialty;
import org.pahappa.systems.enums.Status;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.models.User;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserDAO {
    public void saveRecord(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateRecord(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public User getRecordById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public User getRecordByName(String fullName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE fullName = :name", User.class)
                    .setParameter("name", fullName)
                    .uniqueResult();
        }
    }

    public User getRecordByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // return session.get(User.class, email);
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
    }

    public List<User> getAllRecords() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User u order by u.id desc", User.class).list();
        }
    }

    public void deleteRecord(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                // System.out.println("Student is deleted");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            if (user != null) {
                session.delete(user);
                System.out.println("Student is deleted");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Doctor> getDoctorsBySpecializationAndStatus(Specialty specialization, Status status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Doctor WHERE specialization = :specialization AND staffStatus = :status",
                            Doctor.class)
                    .setParameter("specialization", specialization)
                    .setParameter("status", status)
                    .list();
        }
    }

    public List<Doctor> findDoctorsBySpecialty(Specialty specialty) {
        return getDoctorsBySpecializationAndStatus(specialty, Status.ACTIVE);
    }

    public Doctor findById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Doctor.class, id);
        }
    }
}

package org.pahappa.systems.repository;

import org.pahappa.systems.models.Medicine;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MedicineDAO {

    public void saveRecord(Medicine medicine) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(medicine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateRecord(Medicine medicine) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(medicine);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Medicine getRecordById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Medicine.class, id);
        }
    }

    public List<Medicine> getAllRecords() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Medicine WHERE active = true ORDER BY name", Medicine.class).list();
        }
    }

    public List<Medicine> getAllRecordsIncludingInactive() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Medicine ORDER BY name", Medicine.class).list();
        }
    }

    public void deleteRecord(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Medicine medicine = session.get(Medicine.class, id);
            if (medicine != null) {
                medicine.setActive(false);
                session.merge(medicine);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Medicine findById(Long id) {
        try (org.hibernate.Session session = org.pahappa.systems.util.HibernateUtil.getSessionFactory().openSession()) {
            return session.get(org.pahappa.systems.models.Medicine.class, id);
        }
    }
}
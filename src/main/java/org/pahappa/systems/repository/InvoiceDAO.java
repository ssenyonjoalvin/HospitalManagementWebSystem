package org.pahappa.systems.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pahappa.systems.models.Invoice;
import org.pahappa.systems.models.MedicalReport;
import org.pahappa.systems.util.HibernateUtil;

import java.util.List;

@ApplicationScoped
public class InvoiceDAO {

    /**
     * CORRECTED findAll() METHOD
     * This query now fetches all related data needed for display pages
     * in a single, efficient database call. This will resolve all
     * LazyInitializationExceptions for invoices, patients, doctors, medicines, and services.
     */
    public List<Invoice> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT DISTINCT i FROM Invoice i " +
                            "LEFT JOIN FETCH i.patient " +
                            "LEFT JOIN FETCH i.doctor " +
                            "LEFT JOIN FETCH i.medicines " +
                            "LEFT JOIN FETCH i.services",
                    Invoice.class
            ).list();
        }
    }

    /**
     * CORRECTED findById() METHOD
     * This query fetches a single invoice and all its related data,
     * resolving LazyInitializationExceptions for detail pages.
     */
    public Invoice findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT i FROM Invoice i " +
                            "LEFT JOIN FETCH i.patient " +
                            "LEFT JOIN FETCH i.doctor " +
                            "LEFT JOIN FETCH i.medicines " +
                            "LEFT JOIN FETCH i.services " +
                            "WHERE i.id = :id",
                    Invoice.class
            ).setParameter("id", id).uniqueResult();
        }
    }

    // --- The methods below are for writing/deleting data and do NOT need changes ---

    public void delete(Invoice invoice) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(invoice) ? invoice : session.merge(invoice));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    public void saveOrUpdate(Invoice invoice) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(invoice);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    public static class SaveCheckupResult {
        public final Long medicalReportId;
        public final Long invoiceId;

        public SaveCheckupResult(Long medicalReportId, Long invoiceId) {
            this.medicalReportId = medicalReportId;
            this.invoiceId = invoiceId;
        }
    }

    public SaveCheckupResult saveCheckupAndInvoice(
            org.pahappa.systems.models.Patient patient,
            org.pahappa.systems.models.Doctor doctor,
            String signsAndSymptoms,
            String conclusion,
            String diagnosis,
            String treatmentPlan,
            java.time.LocalDate followUpDate,
            java.util.List<Long> medicineIds,
            java.util.List<Long> serviceIds) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = org.pahappa.systems.util.HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // 1. Create and persist MedicalReport
            MedicalReport medicalReport = new MedicalReport(
                    patient, doctor, signsAndSymptoms, conclusion);
            medicalReport.setDiagnosis(diagnosis);
            medicalReport.setTreatmentPlan(treatmentPlan);
            if (followUpDate != null) {
                medicalReport.setFollowUpDate(followUpDate.atStartOfDay());
            }
            session.persist(medicalReport);

            // 2. Create Invoice and add medicines/services
            org.pahappa.systems.models.Invoice invoice = new org.pahappa.systems.models.Invoice(patient, doctor);
            if (medicineIds != null) {
                for (Long medId : medicineIds) {
                    org.pahappa.systems.models.Medicine med = session.get(org.pahappa.systems.models.Medicine.class,
                            medId);
                    if (med != null)
                        invoice.addMedicine(med);
                }
            }
            if (serviceIds != null) {
                for (Long servId : serviceIds) {
                    org.pahappa.systems.models.Service serv = session.get(org.pahappa.systems.models.Service.class,
                            servId);
                    if (serv != null)
                        invoice.addService(serv);
                }
            }

            session.persist(invoice);

            transaction.commit();
            return new SaveCheckupResult(medicalReport.getId(), invoice.getId());
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            if (session != null && session.isOpen())
                session.close();
        }
    }
}
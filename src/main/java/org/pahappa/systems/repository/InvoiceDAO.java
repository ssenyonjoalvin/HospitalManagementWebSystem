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
    public List<Invoice> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Invoice", Invoice.class).list();
        }
    }

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
            // Debug: Print list sizes and contents before persisting
            System.out.println("[DEBUG] Medicines to persist: " + invoice.getMedicines().size());
            for (org.pahappa.systems.models.Medicine med : invoice.getMedicines()) {
                System.out.println("[DEBUG] Medicine: " + med.getId() + " - " + med.getName() + " - " + med.getPrice());
            }
            System.out.println("[DEBUG] Services to persist: " + invoice.getServices().size());
            for (org.pahappa.systems.models.Service serv : invoice.getServices()) {
                System.out
                        .println("[DEBUG] Service: " + serv.getId() + " - " + serv.getName() + " - " + serv.getPrice());
            }

            session.persist(invoice);
            session.flush(); // Force Hibernate to write join table entries

            // Debug: Print after persist/flush
            System.out.println("[DEBUG] After persist/flush - Medicines: " + invoice.getMedicines().size());
            System.out.println("[DEBUG] After persist/flush - Services: " + invoice.getServices().size());

            // Debug: Print medicines and their prices
            System.out.println("Medicines in invoice:");
            for (org.pahappa.systems.models.Medicine med : invoice.getMedicines()) {
                System.out.println(med.getName() + " - " + med.getPrice());
            }
            // Debug: Print services and their prices
            System.out.println("Services in invoice:");
            for (org.pahappa.systems.models.Service serv : invoice.getServices()) {
                System.out.println(serv.getName() + " - " + serv.getPrice());
            }
            // Debug: Print calculated total
            System.out.println("Calculated total: " + invoice.getTotalAmount());

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
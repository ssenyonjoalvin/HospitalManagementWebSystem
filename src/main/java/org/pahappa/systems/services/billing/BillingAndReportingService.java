package org.pahappa.systems.services.billing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.*;
import org.pahappa.systems.repository.MedicineDAO;
import org.pahappa.systems.repository.ServiceDAO;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@ApplicationScoped
public class BillingAndReportingService {

    @Inject
    private MedicineDAO medicineDAO;

    @Inject
    private ServiceDAO serviceDAO;

    /**
     * Core transactional method to process a patient checkup.
     * Creates both MedicalReport and Invoice in a single atomic transaction.
     */
    public CheckupResult processCheckup(Long patientId, Long doctorId,
            String signsAndSymptoms, String conclusion,
            List<Long> medicineIds, List<Long> serviceIds) {

        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // 1. Fetch Patient and Doctor
            Patient patient = session.get(Patient.class, patientId);
            Doctor doctor = session.get(Doctor.class, doctorId);

            if (patient == null) {
                throw new IllegalArgumentException("Patient not found with ID: " + patientId);
            }
            if (doctor == null) {
                throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
            }

            // 2. Create Medical Report
            MedicalReport medicalReport = new MedicalReport(patient, doctor, signsAndSymptoms, conclusion);
            session.persist(medicalReport);

            // 3. Create Invoice
            Invoice invoice = new Invoice(patient, doctor);

            // 4. Add selected medicines to invoice
            if (medicineIds != null && !medicineIds.isEmpty()) {
                for (Long medicineId : medicineIds) {
                    Medicine medicine = session.get(Medicine.class, medicineId);
                    if (medicine != null) {
                        invoice.addMedicine(medicine);
                    }
                }
            }

            // 5. Add selected services to invoice
            if (serviceIds != null && !serviceIds.isEmpty()) {
                for (Long serviceId : serviceIds) {
                    Service service = session.get(Service.class, serviceId);
                    if (service != null) {
                        invoice.addService(service);
                    }
                }
            }

            // 6. Persist the invoice
            session.persist(invoice);

            // 7. Commit transaction
            transaction.commit();

            // 8. Return success result
            return new CheckupResult(true, "Checkup processed successfully", medicalReport.getId(), invoice.getId());

        } catch (Exception e) {
            // Rollback transaction on any error
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return new CheckupResult(false, "Error processing checkup: " + e.getMessage(), null, null);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Get all available medicines for UI dropdown
     */
    public List<Medicine> getAllMedicines() {
        return medicineDAO.getAllRecords();
    }

    /**
     * Get all available services for UI dropdown
     */
    public List<Service> getAllServices() {
        return serviceDAO.getAllRecords();
    }

    /**
     * Result class for checkup processing
     */
    public static class CheckupResult {
        private boolean success;
        private String message;
        private Long medicalReportId;
        private Long invoiceId;

        public CheckupResult(boolean success, String message, Long medicalReportId, Long invoiceId) {
            this.success = success;
            this.message = message;
            this.medicalReportId = medicalReportId;
            this.invoiceId = invoiceId;
        }

        // Getters
        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Long getMedicalReportId() {
            return medicalReportId;
        }

        public Long getInvoiceId() {
            return invoiceId;
        }
    }
}
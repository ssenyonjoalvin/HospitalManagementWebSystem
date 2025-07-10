package org.pahappa.systems.services.billing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.*;
import org.pahappa.systems.repository.MedicineDAO;
import org.pahappa.systems.repository.ServiceDAO;
import org.pahappa.systems.repository.InvoiceDAO;
import org.pahappa.systems.repository.PatientDAO;
import org.pahappa.systems.repository.DoctorDAO;
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

    @Inject
    private InvoiceDAO invoiceDAO;

    @Inject
    private PatientDAO patientDAO;

    @Inject
    private DoctorDAO doctorDAO;

    /**
     * Core transactional method to process a patient checkup.
     * Creates both MedicalReport and Invoice in a single atomic transaction.
     */
    public CheckupResult processCheckup(Long patientId, Long doctorId,
            String signsAndSymptoms, String conclusion,
            String diagnosis, String treatmentPlan, java.time.LocalDate followUpDate,
            List<Long> medicineIds, List<Long> serviceIds) {
        try {
            // Fetch Patient and Doctor using DAOs
            Patient patient = patientDAO.findById(patientId);
            Doctor doctor = doctorDAO.findById(doctorId);
            if (patient == null)
                throw new IllegalArgumentException("Patient not found with ID: " + patientId);
            if (doctor == null)
                throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);

            // Pass only IDs to the DAO
            InvoiceDAO.SaveCheckupResult result = invoiceDAO.saveCheckupAndInvoice(
                    patient, doctor, signsAndSymptoms, conclusion, diagnosis, treatmentPlan, followUpDate, medicineIds,
                    serviceIds);
            return new CheckupResult(true, "Checkup processed successfully", result.medicalReportId, result.invoiceId);
        } catch (Exception e) {
            e.printStackTrace();
            return new CheckupResult(false, "Error processing checkup: " + e.getMessage(), null, null);
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

    public List<Invoice> findAllInvoices() {
        return invoiceDAO.findAll();
    }

    public void deleteInvoice(Invoice invoice) {
        invoiceDAO.delete(invoice);
    }

    public Invoice findInvoiceById(Long id) {
        return invoiceDAO.findById(id);
    }

    public void cancelInvoice(Invoice invoice) {
        invoiceDAO.saveOrUpdate(invoice);
    }
}
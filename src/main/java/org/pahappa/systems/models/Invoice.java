package org.pahappa.systems.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    @ManyToMany
    @JoinTable(name = "invoice_medicines", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "medicine_id"))
    private List<Medicine> medicines = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "invoice_services", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<Service> services = new ArrayList<>();

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status = InvoiceStatus.PENDING;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum InvoiceStatus {
        PENDING, PAID, OVERDUE, CANCELLED
    }

    public Invoice() {
        this.creationDate = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }

    public Invoice(Patient patient, Doctor doctor) {
        this();
        this.patient = patient;
        this.doctor = doctor;
    }

    // Helper methods
    public void addMedicine(Medicine medicine) {
        if (!medicines.contains(medicine)) {
            medicines.add(medicine);
            calculateTotal();
        }
    }

    public void addService(Service service) {
        if (!services.contains(service)) {
            services.add(service);
            calculateTotal();
        }
    }

    public void removeMedicine(Medicine medicine) {
        medicines.remove(medicine);
        calculateTotal();
    }

    public void removeService(Service service) {
        services.remove(service);
        calculateTotal();
    }

    public void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        // Add medicine costs
        for (Medicine medicine : medicines) {
            total = total.add(medicine.getPrice());
        }

        // Add service costs
        for (Service service : services) {
            total = total.add(service.getPrice());
        }

        this.totalAmount = total;
    }

    public void markAsPaid() {
        this.status = InvoiceStatus.PAID;
        this.paidDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        calculateTotal();
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
        calculateTotal();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
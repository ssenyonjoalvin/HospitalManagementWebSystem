package org.pahappa.systems.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
// Import Set and HashSet
import java.util.HashSet;
import java.util.Set;
import org.pahappa.systems.enums.InvoiceStatus;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Good practice to lazy load ManyToOne unless always needed
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY) // Good practice to lazy load ManyToOne unless always needed
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    // --- CHANGE 1: Swapped List for Set ---
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Cascade added earlier
    @JoinTable(name = "invoice_medicines", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "medicine_id"))
    private Set<Medicine> medicines = new HashSet<>();

    // --- CHANGE 2: Swapped List for Set ---
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Cascade added earlier
    @JoinTable(name = "invoice_services", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services = new HashSet<>();

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public Invoice() {
        this.creationDate = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }

    public Invoice(Patient patient, Doctor doctor) {
        this();
        this.patient = patient;
        this.doctor = doctor;
    }

    // --- CHANGE 3: Helper methods now work with Set ---
    public void addMedicine(Medicine medicine) {
        // The add method of a Set already handles duplicates automatically.
        // It returns true if the element was added, false if it was already present.
        if (this.medicines.add(medicine)) {
            calculateTotal();
        }
    }

    public void addService(Service service) {
        if (this.services.add(service)) {
            calculateTotal();
        }
    }

    public void removeMedicine(Medicine medicine) {
        if (this.medicines.remove(medicine)) {
            calculateTotal();
        }
    }

    public void removeService(Service service) {
        if (this.services.remove(service)) {
            calculateTotal();
        }
    }

    public void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (Medicine medicine : medicines) {
            total = total.add(medicine.getPrice());
        }

        for (Service service : services) {
            total = total.add(service.getPrice());
        }

        this.totalAmount = total;
    }

    public void markAsPaid() {
        this.status = InvoiceStatus.PAID;
        this.paidDate = LocalDateTime.now();
    }

    // --- CHANGE 4: Getters and Setters now use Set ---
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Set<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(Set<Medicine> medicines) {
        this.medicines = medicines;
        calculateTotal();
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
        calculateTotal();
    }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
    public LocalDateTime getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDateTime paidDate) { this.paidDate = paidDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
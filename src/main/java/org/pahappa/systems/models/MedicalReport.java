package org.pahappa.systems.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical-report")
public class MedicalReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Lob
    private String signsAndSymptoms;

    @Lob
    private String conclusion;

    // Getters and setters
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getSignsAndSymptoms() {
        return signsAndSymptoms;
    }

    public void setSignsAndSymptoms(String signsAndSymptoms) {
        this.signsAndSymptoms = signsAndSymptoms;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
}
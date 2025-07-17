package org.pahappa.systems.models;

import jakarta.persistence.Entity;
import org.pahappa.systems.enums.PatientType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
//use compositions
public class Patient extends User {
    @ManyToOne
    @JoinColumn(name = "patient_type_id")
    private PatientTypeEntity patientType;
    private String insuranceNumber;

    
    public PatientTypeEntity getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientTypeEntity patientType) {
        this.patientType = patientType;
    }

    public Patient() {

    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

  

    public Patient(User user) {
        this.setFullName(user.getFullName());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setEmail(user.getEmail());
        this.setDateOfBirth(user.getDateOfBirth());
        this.setAddress(user.getAddress());
        this.setNextOfKin(user.getNextOfKin());
        this.setRole(user.getRole());
        this.setGender(user.getGender());
        this.setDeleted(user.isDeleted());
    }

    @Override
    public String toString() {
        return "ID: " + getId() + "\n"
                + "Name: " + getFullName() + "\n"
                + "Phone Number: " + getPhoneNumber() + "\n"
                + "Email: " + getEmail() + "\n"
                + "Date of Birth: " + getDateOfBirth() + "\n"
                + "Address: " + getAddress() + "\n"
                + "Next of Kin: " + getNextOfKin() + "\n"
                + "Role: " + getRole() + "\n"
                + "Gender: " + getGender() + "\n"
                + "Insurance Number: " + insuranceNumber + "\n";
    }
}

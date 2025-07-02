package org.pahappa.systems.models;

import jakarta.persistence.Entity;
import org.pahappa.systems.enums.PatientType;

@Entity

public class Patient extends User {
    private PatientType patientType;
    private String insuranceNumber;

    
    public PatientType getPatientType() {
        return patientType;
    }

    public void setPatientType(PatientType patientType) {
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
        this.setPassword(user.getPassword());
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

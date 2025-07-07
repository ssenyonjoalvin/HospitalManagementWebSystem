package org.pahappa.systems.models;

import jakarta.persistence.Entity;
import org.pahappa.systems.enums.Gender;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Shift;

import java.time.LocalDate;

@Entity
public class Pharmacist extends User {
    private String licenseNumber;
    private Shift shift;


    public Pharmacist() {

    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Pharmacist(Rolename role, String password, String nextOfKin, String address, Gender gender,
                      LocalDate dateOfBirth,
                      String email, String phoneNumber, String fullName, String licenseNumber, Shift shift) {
        super(role, password, nextOfKin, address, gender, dateOfBirth, email, phoneNumber, fullName);
        this.licenseNumber = licenseNumber;
        this.shift = shift;
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
                + "License Number: " + licenseNumber + "\n"
                + "Shift: " + shift + "\n";
    }

}

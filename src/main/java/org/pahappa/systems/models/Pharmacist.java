package org.pahappa.systems.models;

import jakarta.persistence.Entity;
import org.pahappa.systems.enums.Gender;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Shift;

import java.time.LocalDate;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import org.pahappa.systems.models.LoginCapable;

@Entity
public class Pharmacist extends User implements LoginCapable {
    private String licenseNumber;
    private Shift shift;
    @OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;


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

    @Override
    public UserAccount getUserAccount() {
        return userAccount;
    }
    @Override
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Pharmacist(Rolename role, String nextOfKin, String address, Gender gender,
                      LocalDate dateOfBirth, String email, String phoneNumber, String fullName, String licenseNumber, Shift shift, UserAccount userAccount) {
        super(role, nextOfKin, address, gender, dateOfBirth, email, phoneNumber, fullName);
        this.licenseNumber = licenseNumber;
        this.shift = shift;
        this.userAccount = userAccount;
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

    @Override
    public String getPassword() {
        return userAccount != null ? userAccount.getPassword() : null;
    }
    @Override
    public void setPassword(String password) {
        if (userAccount != null) userAccount.setPassword(password);
    }

    @Override
    public String getUsername() {
        return userAccount != null ? userAccount.getUserName() : null;
    }
    @Override
    public void setUsername(String username) {
        if (userAccount != null) userAccount.setUserName(username);
    }
}

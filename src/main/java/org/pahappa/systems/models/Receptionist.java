package org.pahappa.systems.models;

import org.pahappa.systems.enums.Gender;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Shift;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import java.time.LocalDate;

@Entity
public class Receptionist extends User {
    private String deskNumber;
    private Shift shift;
    @OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;


    public Receptionist() {
    }

    public Receptionist(Rolename role, String nextOfKin, String address, Gender gender,
            LocalDate dateOfBirth, String email, String phoneNumber, String fullName, String deskNumber, Shift shift, UserAccount userAccount) {
        super(role, nextOfKin, address, gender, dateOfBirth, email, phoneNumber, fullName);
        this.deskNumber = deskNumber;
        this.shift = shift;
        this.userAccount = userAccount;
    }

    public String getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(String deskNumber) {
        this.deskNumber = deskNumber;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
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
                + "Desk Number: " + deskNumber + "\n"
                + "Shift: " + shift + "\n";
    }
}

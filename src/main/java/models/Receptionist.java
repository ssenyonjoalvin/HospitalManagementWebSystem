package models;

import enums.Gender;
import enums.Rolename;
import enums.Shift;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Receptionist extends User {
    private String deskNumber;
    private Shift shift;

    public Receptionist() {
    }

    public Receptionist(Rolename role, String password, String nextOfKin, String address, Gender gender,
            LocalDate dateOfBirth, String email, String phoneNumber, String fullName, String deskNumber, Shift shift) {
        super(role, password, nextOfKin, address, gender, dateOfBirth, email, phoneNumber, fullName);
        this.deskNumber = deskNumber;
        this.shift = shift;
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

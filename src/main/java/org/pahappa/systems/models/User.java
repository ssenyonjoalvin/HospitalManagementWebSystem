package org.pahappa.systems.models;

import org.pahappa.systems.enums.Gender;
import org.pahappa.systems.enums.Rolename;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String nextOfKin;

    private String password;
    @Enumerated(EnumType.STRING)
    private Rolename role;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean deleted = false;

    public User(Rolename role, String password, String nextOfKin, String address, Gender gender, LocalDate dateOfBirth,
            String email, String phoneNumber, String fullName) {
        this.role = role;
        this.gender = gender;
        this.password = password;
        this.nextOfKin = nextOfKin;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;

    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rolename getRole() {
        return role;
    }

    public void setRole(Rolename role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        address = address;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", nextOfKin='" + nextOfKin + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", gender=" + gender +
                ", deleted=" + deleted +
                '}';
    }
}

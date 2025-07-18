package org.pahappa.systems.models;

import org.pahappa.systems.enums.*;
import org.pahappa.systems.models.UserAccount;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="doctors")
public class Doctor extends User {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;

    @Enumerated(EnumType.STRING)
    private Specialty specialization;

    @Enumerated(EnumType.STRING)
    private Qualification qualification;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status staffStatus;
    private int yearsOfExperience;

    // Constructor must include staffStatus too if you use it
    public Doctor(Rolename role, String nextOfKin, String address, Gender gender,
            LocalDate dateOfBirth,
            String email, String phoneNumber, String fullName, Specialty specialization,
            Qualification qualification, Department department, int yearsOfExperience, Status staffStatus, UserAccount userAccount) {
        super(role, nextOfKin, address, gender, dateOfBirth, email, phoneNumber, fullName);
        this.specialization = specialization;
        this.qualification = qualification;
        this.department = department;
        this.yearsOfExperience = yearsOfExperience;
        this.staffStatus = staffStatus;
        this.userAccount = userAccount;
    }

    public Doctor() {
        // required by Hibernate
    }

    // getters and setters
    // ... existing constructors

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    public Specialty getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialty specialization) {
        this.specialization = specialization;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Status getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(Status staffStatus) {
        this.staffStatus = staffStatus;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void setStatus(Status status) {
        this.staffStatus = status;
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
                + "Specialization: " + specialization + "\n"
                + "Qualification: " + qualification + "\n"
                + "Department: " + department + "\n"
                + "Status: " + staffStatus + "\n"
                + "Years of Experience: " + yearsOfExperience + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Doctor doctor = (Doctor) o;
        return this.getId() == doctor.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

}

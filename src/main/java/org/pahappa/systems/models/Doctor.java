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

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private SpecialtyEntity specialization;

    @ManyToOne
    @JoinColumn(name = "qualification_id")
    private QualificationEntity qualification;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToOne
    @JoinColumn(name = "staff_status_id")
    private StatusEntity staffStatus;
    private int yearsOfExperience;

    // Constructor must include staffStatus too if you use it
    public Doctor(Role role, String nextOfKin, String address, Gender gender,
            LocalDate dateOfBirth,
            String email, String phoneNumber, String fullName, SpecialtyEntity specialization,
            QualificationEntity qualification, DepartmentEntity department, int yearsOfExperience, StatusEntity staffStatus, UserAccount userAccount) {
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
    public SpecialtyEntity getSpecialization() {
        return specialization;
    }

    public void setSpecialization(SpecialtyEntity specialization) {
        this.specialization = specialization;
    }

    public QualificationEntity getQualification() {
        return qualification;
    }

    public void setQualification(QualificationEntity qualification) {
        this.qualification = qualification;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public StatusEntity getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(StatusEntity staffStatus) {
        this.staffStatus = staffStatus;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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

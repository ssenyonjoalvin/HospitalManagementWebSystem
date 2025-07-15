package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import org.pahappa.systems.enums.*;
import org.pahappa.systems.models.User;
import org.pahappa.systems.services.user.UserService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;

@Named("userRegistrationBean")
@SessionScoped //

public class UserRegistrationBean implements Serializable {
    private User user = new User();
    private Rolename selectedRole;
    private Gender selectedGender;
    private List<Rolename> roles;
    private List<Gender> genders;
    // Doctor fields
    private Specialty specialization;
    private Qualification qualification;
    private Department department;
    private Status staffStatus;
    private Integer yearsOfExperience;
    // Receptionist fields
    private String deskNumber;
    private Shift receptionistShift;
    // Pharmacist fields
    private String licenseNumber;
    private Shift pharmacistShift;
    // Dropdowns
    private List<Specialty> specialties;
    private List<Qualification> qualifications;
    private List<Department> departments;
    private List<Status> statuses;
    private List<Shift> shifts;
    private String message;
    private String userName;
    private String password;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        roles = Arrays.asList(Rolename.DOCTOR, Rolename.PHARMACIST, Rolename.RECEPTIONIST);
        genders = Arrays.asList(Gender.values());
        specialties = Arrays.asList(Specialty.values());
        qualifications = Arrays.asList(Qualification.values());
        departments = Arrays.asList(Department.values());
        statuses = Arrays.asList(Status.values());
        shifts = Arrays.asList(Shift.values());
    }

    public String register() {
        try {
            // Create UserAccount for the new employee
            org.pahappa.systems.models.UserAccount userAccount = new org.pahappa.systems.models.UserAccount();
            userAccount.setUserName(userName);
            userAccount.setPassword(password);

            userService.registerEmployee(
                user,
                selectedRole,
                specialization,
                qualification,
                department,
                staffStatus,
                yearsOfExperience,
                deskNumber,
                receptionistShift,
                licenseNumber,
                pharmacistShift,
                userAccount // pass the UserAccount
            );
            // Reset form fields
            user = new org.pahappa.systems.models.User();
            selectedRole = null;
            specialization = null;
            qualification = null;
            department = null;
            staffStatus = null;
            yearsOfExperience = null;
            deskNumber = null;
            receptionistShift = null;
            licenseNumber = null;
            pharmacistShift = null;
            userName = null;
            password = null;
            message = "Employee registered successfully!";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
            return "employee.xhtml?faces-redirect=true";
        } catch (Exception e) {
            message = "Error registering employee: " + e.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return null;
        }
    }

    // Getters and setters for all fields
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rolename getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Rolename selectedRole) {
        this.selectedRole = selectedRole;
    }

    public Gender getSelectedGender() {
        return selectedGender;
    }

    public void setSelectedGender(Gender selectedGender) {
        this.selectedGender = selectedGender;
    }

    public List<Rolename> getRoles() {
        return roles;
    }

    public List<Gender> getGenders() {
        return genders;
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

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getDeskNumber() {
        return deskNumber;
    }

    public void setDeskNumber(String deskNumber) {
        this.deskNumber = deskNumber;
    }

    public Shift getReceptionistShift() {
        return receptionistShift;
    }

    public void setReceptionistShift(Shift receptionistShift) {
        this.receptionistShift = receptionistShift;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Shift getPharmacistShift() {
        return pharmacistShift;
    }

    public void setPharmacistShift(Shift pharmacistShift) {
        this.pharmacistShift = pharmacistShift;
    }

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for today's date for date picker validation
    public LocalDate getNow() {
        return java.time.LocalDate.now();
    }
}
package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import org.pahappa.systems.enums.*;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.Role;
import org.pahappa.systems.models.SpecialtyEntity;
import org.pahappa.systems.models.QualificationEntity;
import org.pahappa.systems.models.DepartmentEntity;
import org.pahappa.systems.models.StatusEntity;
import org.pahappa.systems.models.ShiftEntity;
import org.pahappa.systems.services.user.UserService;
import org.pahappa.systems.services.RoleService;
import org.pahappa.systems.services.SpecialtyEntityService;
import org.pahappa.systems.services.QualificationEntityService;
import org.pahappa.systems.services.DepartmentEntityService;
import org.pahappa.systems.services.StatusEntityService;
import org.pahappa.systems.services.ShiftEntityService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;

@Named("userRegistrationBean")
@SessionScoped //

public class UserRegistrationBean implements Serializable {
    private User user = new User();
    private Role selectedRole;
    private Gender selectedGender;
    private List<Role> roles;
    private List<Gender> genders;
    // Doctor fields
    private SpecialtyEntity specialization;
    private QualificationEntity qualification;
    private DepartmentEntity department;
    private StatusEntity staffStatus;
    private Integer yearsOfExperience;
    // Receptionist fields
    private String deskNumber;
    private ShiftEntity receptionistShift;
    // Pharmacist fields
    private String licenseNumber;
    private ShiftEntity pharmacistShift;
    // Dropdowns
    private List<SpecialtyEntity> specialties;
    private List<QualificationEntity> qualifications;
    private List<DepartmentEntity> departments;
    private List<StatusEntity> statuses;
    private List<ShiftEntity> shifts;
    private String message;
    private String userName;
    private String password;

    @Inject
    private UserService userService;
    @Inject
    private RoleService roleService;
    @Inject
    private SpecialtyEntityService specialtyEntityService;
    @Inject
    private QualificationEntityService qualificationEntityService;
    @Inject
    private DepartmentEntityService departmentEntityService;
    @Inject
    private StatusEntityService statusEntityService;
    @Inject
    private ShiftEntityService shiftEntityService;

    @PostConstruct
    public void init() {
        roles = roleService.getAll();
        specialties = specialtyEntityService.getAll();
        qualifications = qualificationEntityService.getAll();
        departments = departmentEntityService.getAll();
        statuses = statusEntityService.getAll();
        shifts = shiftEntityService.getAll();
        // genders can remain as enum
        genders = Arrays.asList(Gender.values());
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

    public Role getSelectedRole() { return selectedRole; }
    public void setSelectedRole(Role selectedRole) { this.selectedRole = selectedRole; }
    public List<Role> getRoles() { return roles; }
    public Gender getSelectedGender() {
        return selectedGender;
    }

    public void setSelectedGender(Gender selectedGender) {
        this.selectedGender = selectedGender;
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public SpecialtyEntity getSpecialization() { return specialization; }
    public void setSpecialization(SpecialtyEntity specialization) { this.specialization = specialization; }
    public List<SpecialtyEntity> getSpecialties() { return specialties; }
    public QualificationEntity getQualification() { return qualification; }
    public void setQualification(QualificationEntity qualification) { this.qualification = qualification; }
    public List<QualificationEntity> getQualifications() { return qualifications; }
    public DepartmentEntity getDepartment() { return department; }
    public void setDepartment(DepartmentEntity department) { this.department = department; }
    public List<DepartmentEntity> getDepartments() { return departments; }
    public StatusEntity getStaffStatus() { return staffStatus; }
    public void setStaffStatus(StatusEntity staffStatus) { this.staffStatus = staffStatus; }
    public List<StatusEntity> getStatuses() { return statuses; }
    public ShiftEntity getReceptionistShift() { return receptionistShift; }
    public void setReceptionistShift(ShiftEntity receptionistShift) { this.receptionistShift = receptionistShift; }
    public ShiftEntity getPharmacistShift() { return pharmacistShift; }
    public void setPharmacistShift(ShiftEntity pharmacistShift) { this.pharmacistShift = pharmacistShift; }
    public List<ShiftEntity> getShifts() { return shifts; }

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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
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
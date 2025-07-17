package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import org.pahappa.systems.models.*;
import org.pahappa.systems.services.*;
import java.io.Serializable;
import java.util.List;
import org.pahappa.systems.models.PatientTypeEntity;
import org.pahappa.systems.services.PatientTypeEntityService;

@Named("adminSettingsBean")
@SessionScoped
public class AdminSettingsBean implements Serializable {
    // Role
    @Inject private RoleService roleService;
    private List<Role> roles;
    private Role selectedRole = new Role();
    private boolean editRole = false;

    // Specialty
    @Inject private SpecialtyEntityService specialtyEntityService;
    private List<SpecialtyEntity> specialties;
    private SpecialtyEntity selectedSpecialty = new SpecialtyEntity();
    private boolean editSpecialty = false;

    // Qualification
    @Inject private QualificationEntityService qualificationEntityService;
    private List<QualificationEntity> qualifications;
    private QualificationEntity selectedQualification = new QualificationEntity();
    private boolean editQualification = false;

    // Department
    @Inject private DepartmentEntityService departmentEntityService;
    private List<DepartmentEntity> departments;
    private DepartmentEntity selectedDepartment = new DepartmentEntity();
    private boolean editDepartment = false;

    // Status
    @Inject private StatusEntityService statusEntityService;
    private List<StatusEntity> statuses;
    private StatusEntity selectedStatus = new StatusEntity();
    private boolean editStatus = false;

    // Shift
    @Inject private ShiftEntityService shiftEntityService;
    private List<ShiftEntity> shifts;
    private ShiftEntity selectedShift = new ShiftEntity();
    private boolean editShift = false;

    @Inject private PatientTypeEntityService patientTypeEntityService;
    private List<PatientTypeEntity> patientTypes;
    private PatientTypeEntity selectedPatientType = new PatientTypeEntity();
    private boolean editPatientType = false;

    @PostConstruct
    public void init() {
        refreshAll();
    }
    public void refreshAll() {
        roles = roleService.getAll();
        specialties = specialtyEntityService.getAll();
        qualifications = qualificationEntityService.getAll();
        departments = departmentEntityService.getAll();
        statuses = statusEntityService.getAll();
        shifts = shiftEntityService.getAll();
        patientTypes = patientTypeEntityService.getAll();
    }
    // --- Role CRUD ---
    public void saveRole() {
        if (editRole) roleService.update(selectedRole);
        else roleService.save(selectedRole);
        selectedRole = new Role();
        editRole = false;
        refreshAll();
    }
    public void editRole(Role role) {
        this.selectedRole = role;
        this.editRole = true;
    }
    public void deleteRole(Role role) {
        roleService.delete(role.getId());
        refreshAll();
    }
    // --- Specialty CRUD ---
    public void saveSpecialty() {
        if (editSpecialty) specialtyEntityService.update(selectedSpecialty);
        else specialtyEntityService.save(selectedSpecialty);
        selectedSpecialty = new SpecialtyEntity();
        editSpecialty = false;
        refreshAll();
    }
    public void editSpecialty(SpecialtyEntity s) {
        this.selectedSpecialty = s;
        this.editSpecialty = true;
    }
    public void deleteSpecialty(SpecialtyEntity s) {
        specialtyEntityService.delete(s.getId());
        refreshAll();
    }
    // --- Qualification CRUD ---
    public void saveQualification() {
        if (editQualification) qualificationEntityService.update(selectedQualification);
        else qualificationEntityService.save(selectedQualification);
        selectedQualification = new QualificationEntity();
        editQualification = false;
        refreshAll();
    }
    public void editQualification(QualificationEntity q) {
        this.selectedQualification = q;
        this.editQualification = true;
    }
    public void deleteQualification(QualificationEntity q) {
        qualificationEntityService.delete(q.getId());
        refreshAll();
    }
    // --- Department CRUD ---
    public void saveDepartment() {
        if (editDepartment) departmentEntityService.update(selectedDepartment);
        else departmentEntityService.save(selectedDepartment);
        selectedDepartment = new DepartmentEntity();
        editDepartment = false;
        refreshAll();
    }
    public void editDepartment(DepartmentEntity d) {
        this.selectedDepartment = d;
        this.editDepartment = true;
    }
    public void deleteDepartment(DepartmentEntity d) {
        departmentEntityService.delete(d.getId());
        refreshAll();
    }
    // --- Status CRUD ---
    public void saveStatus() {
        if (editStatus) statusEntityService.update(selectedStatus);
        else statusEntityService.save(selectedStatus);
        selectedStatus = new StatusEntity();
        editStatus = false;
        refreshAll();
    }
    public void editStatus(StatusEntity s) {
        this.selectedStatus = s;
        this.editStatus = true;
    }
    public void deleteStatus(StatusEntity s) {
        statusEntityService.delete(s.getId());
        refreshAll();
    }
    // --- Shift CRUD ---
    public void saveShift() {
        if (editShift) shiftEntityService.update(selectedShift);
        else shiftEntityService.save(selectedShift);
        selectedShift = new ShiftEntity();
        editShift = false;
        refreshAll();
    }
    public void editShift(ShiftEntity s) {
        this.selectedShift = s;
        this.editShift = true;
    }
    public void deleteShift(ShiftEntity s) {
        shiftEntityService.delete(s.getId());
        refreshAll();
    }
    // --- PatientType CRUD ---
    public void savePatientType() {
        if (editPatientType) patientTypeEntityService.update(selectedPatientType);
        else patientTypeEntityService.save(selectedPatientType);
        selectedPatientType = new PatientTypeEntity();
        editPatientType = false;
        refreshAll();
    }
    public void editPatientType(PatientTypeEntity p) {
        this.selectedPatientType = p;
        this.editPatientType = true;
    }
    public void deletePatientType(PatientTypeEntity p) {
        patientTypeEntityService.delete(p.getId());
        refreshAll();
    }
    // --- Getters/Setters ---
    public List<Role> getRoles() { return roles; }
    public Role getSelectedRole() { return selectedRole; }
    public void setSelectedRole(Role r) { this.selectedRole = r; }
    public boolean isEditRole() { return editRole; }
    public void setEditRole(boolean b) { this.editRole = b; }

    public List<SpecialtyEntity> getSpecialties() { return specialties; }
    public SpecialtyEntity getSelectedSpecialty() { return selectedSpecialty; }
    public void setSelectedSpecialty(SpecialtyEntity s) { this.selectedSpecialty = s; }
    public boolean isEditSpecialty() { return editSpecialty; }
    public void setEditSpecialty(boolean b) { this.editSpecialty = b; }

    public List<QualificationEntity> getQualifications() { return qualifications; }
    public QualificationEntity getSelectedQualification() { return selectedQualification; }
    public void setSelectedQualification(QualificationEntity q) { this.selectedQualification = q; }
    public boolean isEditQualification() { return editQualification; }
    public void setEditQualification(boolean b) { this.editQualification = b; }

    public List<DepartmentEntity> getDepartments() { return departments; }
    public DepartmentEntity getSelectedDepartment() { return selectedDepartment; }
    public void setSelectedDepartment(DepartmentEntity d) { this.selectedDepartment = d; }
    public boolean isEditDepartment() { return editDepartment; }
    public void setEditDepartment(boolean b) { this.editDepartment = b; }

    public List<StatusEntity> getStatuses() { return statuses; }
    public StatusEntity getSelectedStatus() { return selectedStatus; }
    public void setSelectedStatus(StatusEntity s) { this.selectedStatus = s; }
    public boolean isEditStatus() { return editStatus; }
    public void setEditStatus(boolean b) { this.editStatus = b; }

    public List<ShiftEntity> getShifts() { return shifts; }
    public ShiftEntity getSelectedShift() { return selectedShift; }
    public void setSelectedShift(ShiftEntity s) { this.selectedShift = s; }
    public boolean isEditShift() { return editShift; }
    public void setEditShift(boolean b) { this.editShift = b; }

    public List<PatientTypeEntity> getPatientTypes() { return patientTypes; }
    public PatientTypeEntity getSelectedPatientType() { return selectedPatientType; }
    public void setSelectedPatientType(PatientTypeEntity p) { this.selectedPatientType = p; }
    public boolean isEditPatientType() { return editPatientType; }
    public void setEditPatientType(boolean b) { this.editPatientType = b; }
} 
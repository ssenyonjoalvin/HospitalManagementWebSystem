package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.Role;
import org.pahappa.systems.models.User;
import org.pahappa.systems.services.user.UserService;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named("employeeBean")
@ViewScoped
public class EmployeeBean implements Serializable {
    private List<User> allEmployees;
    private List<User> filteredEmployees;
    private Role roleFilter;
    private User newEmployee = new User();
    private User selectedEmployee;
    private String message;
    private User employeeToDelete;
    private Long employeeId;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        loadEmployees();
    }

    public void loadEmployees() {
        // This should fetch all users who are employees (not patients)
        allEmployees = userService.getAllEmployees().stream()
                .filter(e -> !e.isDeleted())
                .collect(Collectors.toList());
        filteredEmployees = allEmployees;
    }

    public void filterByRole() {
        if (roleFilter == null) {
            filteredEmployees = allEmployees;
        } else {
            filteredEmployees = allEmployees.stream()
                    .filter(e -> e.getRole() != null && roleFilter != null && e.getRole().getName().equals(roleFilter.getName()))
                    .collect(Collectors.toList());
        }
    }

    public void saveEmployee() {
        // TODO: Implement save logic
        message = "Employee saved (stub).";
        loadEmployees(); // Refresh list after save
    }

    public String editEmployee(User employee) {
        this.selectedEmployee = employee;
        return "edit-employee.xhtml?faces-redirect=true";
    }

    public void updateEmployee() {
        userService.updateEmployee(selectedEmployee);
        loadEmployees();
        message = "Employee updated successfully!";
    }

    public void deleteEmployee(User employee) {
        employee.setDeleted(true);
        userService.updateEmployee(employee); // Soft delete
        loadEmployees();
        message = "Employee deleted successfully!";
    }

    public void confirmDelete(User employee) {
        this.employeeToDelete = employee;
    }

    public User getEmployeeToDelete() {
        return employeeToDelete;
    }

    public void setEmployeeToDelete(User employeeToDelete) {
        this.employeeToDelete = employeeToDelete;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
        if (employeeId != null) {
            this.selectedEmployee = userService.getEmployeeById(employeeId);
        }
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    // Getters and setters
    public List<User> getAllEmployees() {
        return allEmployees;
    }

    public List<User> getFilteredEmployees() {
        return filteredEmployees;
    }

    public Role getRoleFilter() {
        return roleFilter;
    }

    public void setRoleFilter(Role roleFilter) {
        this.roleFilter = roleFilter;
    }

    public User getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(User newEmployee) {
        this.newEmployee = newEmployee;
    }

    public User getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(User selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
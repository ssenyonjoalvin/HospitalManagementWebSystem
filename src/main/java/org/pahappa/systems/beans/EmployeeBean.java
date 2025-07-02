package org.pahappa.systems.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.models.User;
import org.pahappa.systems.services.UserService;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named("employeeBean")
@SessionScoped
public class EmployeeBean implements Serializable {
    private List<User> allEmployees;
    private List<User> filteredEmployees;
    private Rolename roleFilter;
    private User newEmployee = new User();
    private User selectedEmployee;
    private String message;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        loadEmployees();
    }

    public void loadEmployees() {
        // This should fetch all users who are employees (not patients)
        allEmployees = userService.getAllEmployees();
        filteredEmployees = allEmployees;
    }

    public void filterByRole() {
        if (roleFilter == null) {
            filteredEmployees = allEmployees;
        } else {
            filteredEmployees = allEmployees.stream()
                    .filter(e -> e.getRole() == roleFilter)
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
        userService.deleteEmployee(employee);
        loadEmployees();
        message = "Employee deleted successfully!";
    }

    // Getters and setters
    public List<User> getAllEmployees() {
        return allEmployees;
    }

    public List<User> getFilteredEmployees() {
        return filteredEmployees;
    }

    public Rolename getRoleFilter() {
        return roleFilter;
    }

    public void setRoleFilter(Rolename roleFilter) {
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
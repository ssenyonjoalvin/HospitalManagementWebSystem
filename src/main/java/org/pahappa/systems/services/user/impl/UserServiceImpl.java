package org.pahappa.systems.services.user.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Specialty;
import org.pahappa.systems.enums.Qualification;
import org.pahappa.systems.enums.Department;
import org.pahappa.systems.enums.Status;
import org.pahappa.systems.enums.Shift;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.models.Pharmacist;
import org.pahappa.systems.models.Receptionist;
import org.pahappa.systems.repository.UserDAO;
import org.pahappa.systems.services.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

// The @ApplicationScoped annotation would now be IGNORED by UserLoginBean,
// because you are not INJECTING it, you are CREATING it manually.
@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public User login(String email, String password) {
        User user = userDAO.getRecordByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public List<User> getAllEmployees() {
        List<User> allUsers = userDAO.getAllRecords();
        return allUsers.stream()
                .filter(u -> u.getRole() != null && (u.getRole().equals(Rolename.DOCTOR) ||
                        u.getRole().equals(Rolename.PHARMACIST) ||
                        u.getRole().equals(Rolename.RECEPTIONIST)))
                .collect(Collectors.toList());
    }

    @Override
    public void registerEmployee(
            User user,
            Rolename selectedRole,
            Specialty specialization,
            Qualification qualification,
            Department department,
            Status staffStatus,
            Integer yearsOfExperience,
            String deskNumber,
            Shift receptionistShift,
            String licenseNumber,
            Shift pharmacistShift) {
        User employee = null;
        switch (selectedRole) {
            case DOCTOR:
                employee = new Doctor(
                        selectedRole,
                        user.getPassword(),
                        user.getNextOfKin(),
                        user.getAddress(),
                        user.getGender(),
                        user.getDateOfBirth(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getFullName(),
                        specialization,
                        qualification,
                        department,
                        yearsOfExperience != null ? yearsOfExperience : 0,
                        staffStatus);
                break;
            case PHARMACIST:
                employee = new Pharmacist(
                        selectedRole,
                        user.getPassword(),
                        user.getNextOfKin(),
                        user.getAddress(),
                        user.getGender(),
                        user.getDateOfBirth(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getFullName(),
                        licenseNumber,
                        pharmacistShift);
                break;
            case RECEPTIONIST:
                employee = new Receptionist(
                        selectedRole,
                        user.getPassword(),
                        user.getNextOfKin(),
                        user.getAddress(),
                        user.getGender(),
                        user.getDateOfBirth(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getFullName(),
                        deskNumber,
                        receptionistShift);
                break;
            default:
                throw new IllegalArgumentException("Invalid role");
        }
        userDAO.saveRecord(employee);
    }

    @Override
    public void updateEmployee(User user) {
        userDAO.updateRecord(user);
    }

    @Override
    public void deleteEmployee(User user) {
        userDAO.deleteRecord(user.getId());
    }

    @Override
    public User getEmployeeById(Long id) {
        return userDAO.getRecordById(id);
    }
}
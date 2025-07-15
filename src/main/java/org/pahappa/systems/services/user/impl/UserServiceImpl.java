package org.pahappa.systems.services.user.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Specialty;
import org.pahappa.systems.enums.Qualification;
import org.pahappa.systems.enums.Department;
import org.pahappa.systems.enums.Status;
import org.pahappa.systems.enums.Shift;
import org.pahappa.systems.models.*;
import org.pahappa.systems.repository.*;
import org.pahappa.systems.services.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

// The @ApplicationScoped annotation would now be IGNORED by UserLoginBean,
// because you are not INJECTING it, you are CREATING it manually.
@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO = new UserDAO();
    private final UserAccountDAO userAccountDAO = new UserAccountDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final ReceptionistDAO receptionistDAO = new ReceptionistDAO();
    private final PharmacistDAO pharmacistDAO = new PharmacistDAO();
    private final AdministratorDAO administratorDAO = new AdministratorDAO();

    @Override
    public User login(String userName, String password) {
        System.out.println("[DEBUG]: Am here at the !!!!!"+password );

        UserAccount userAccount = userAccountDAO.getByUserName(userName);

        if (userAccount != null && userAccount.getPassword().equals(password)) {
            // Try to find the user by UserAccount in each user type
            Doctor doctor = doctorDAO.getByUserAccount(userAccount);
            if (doctor != null) return doctor;
            Receptionist receptionist = receptionistDAO.getByUserAccount(userAccount);
            if (receptionist != null) return receptionist;
            Pharmacist pharmacist = pharmacistDAO.getByUserAccount(userAccount);
            if (pharmacist != null) return pharmacist;
            return administratorDAO. getByUserAccount(userAccount);
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
                .sorted((u1, u2) -> Long.compare(u2.getId(), u1.getId())) // Sort by id descending
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
            Shift pharmacistShift,
            UserAccount userAccount) {
        User employee = null;
        switch (selectedRole) {
            case DOCTOR:
                employee = new Doctor(
                        selectedRole,
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
                        staffStatus,
                        userAccount);
                break;
            case PHARMACIST:
                employee = new Pharmacist(
                        selectedRole,
                        user.getNextOfKin(),
                        user.getAddress(),
                        user.getGender(),
                        user.getDateOfBirth(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getFullName(),
                        licenseNumber,
                        pharmacistShift,
                        userAccount);
                break;
            case RECEPTIONIST:
                employee = new Receptionist(
                        selectedRole,
                        user.getNextOfKin(),
                        user.getAddress(),
                        user.getGender(),
                        user.getDateOfBirth(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getFullName(),
                        deskNumber,
                        receptionistShift,
                        userAccount);
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
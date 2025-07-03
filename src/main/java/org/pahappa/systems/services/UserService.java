package org.pahappa.systems.services;

import org.pahappa.systems.models.User;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Specialty;
import org.pahappa.systems.enums.Qualification;
import org.pahappa.systems.enums.Department;
import org.pahappa.systems.enums.Status;
import org.pahappa.systems.enums.Shift;

import java.util.List;

public interface UserService {
    User user = null;

    default User login(String email, String password) {
        return user;
    };

    default void register(User user) {
    };

    List<User> getAllEmployees();

    void registerEmployee(
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
            Shift pharmacistShift);

    void updateEmployee(User user);

    void deleteEmployee(User user);
}
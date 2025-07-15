package org.pahappa.systems.services.user;

import org.pahappa.systems.models.User;
import org.pahappa.systems.enums.Rolename;
import org.pahappa.systems.enums.Specialty;
import org.pahappa.systems.enums.Qualification;
import org.pahappa.systems.enums.Department;
import org.pahappa.systems.enums.Status;
import org.pahappa.systems.enums.Shift;
import org.pahappa.systems.models.UserAccount;

import java.util.List;

public interface UserService {
    User user = null;

    User login(String userName, String password);

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
            Shift pharmacistShift,
            UserAccount userAccount);

    void updateEmployee(User user);

    void deleteEmployee(User user);

    User getEmployeeById(Long id);
}
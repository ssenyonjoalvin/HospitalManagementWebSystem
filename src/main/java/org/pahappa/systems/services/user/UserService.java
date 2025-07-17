package org.pahappa.systems.services.user;

import org.pahappa.systems.models.User;

import org.pahappa.systems.models.UserAccount;
import org.pahappa.systems.models.Role;
import org.pahappa.systems.models.SpecialtyEntity;
import org.pahappa.systems.models.QualificationEntity;
import org.pahappa.systems.models.DepartmentEntity;
import org.pahappa.systems.models.StatusEntity;
import org.pahappa.systems.models.ShiftEntity;

import java.util.List;

public interface UserService {
    User user = null;

    User login(String userName, String password);

    default void register(User user) {
    };

    List<User> getAllEmployees();

    void registerEmployee(
            User user,
            Role selectedRole,
            SpecialtyEntity specialization,
            QualificationEntity qualification,
            DepartmentEntity department,
            StatusEntity staffStatus,
            Integer yearsOfExperience,
            String deskNumber,
            ShiftEntity receptionistShift,
            String licenseNumber,
            ShiftEntity pharmacistShift,
            UserAccount userAccount);

    void updateEmployee(User user);

    void deleteEmployee(User user);

    User getEmployeeById(Long id);
}
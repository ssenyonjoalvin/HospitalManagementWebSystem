package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.pahappa.systems.models.User;
import org.pahappa.systems.repository.UserDAO;
import org.pahappa.systems.services.UserService;
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
}
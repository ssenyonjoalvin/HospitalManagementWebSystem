package services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import models.User;
import repository.UserDAO;
import services.UserService;
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
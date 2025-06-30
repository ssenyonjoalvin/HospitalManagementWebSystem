package services.impl;

import models.User;
import repository.UserDAO;
import services.UserService;

public class DoctorRegistrationServiceImpl implements UserService {
    private UserDAO userDAO = new UserDAO();

 
    @Override
    public void register(User user) {
        // Add doctor-specific registration logic here if needed
        userDAO.saveRecord(user);
    }
}
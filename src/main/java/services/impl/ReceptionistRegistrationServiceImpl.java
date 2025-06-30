package services.impl;

import models.User;
import repository.UserDAO;
import services.UserService;

public class ReceptionistRegistrationServiceImpl implements UserService {
    private UserDAO userDAO = new UserDAO();


    @Override
    public void register(User user) {
        // Add receptionist-specific registration logic here if needed
        userDAO.saveRecord(user);
    }
}
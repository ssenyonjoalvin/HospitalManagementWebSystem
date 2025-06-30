package services.impl;

import models.User;
import repository.UserDAO;
import services.UserService;

public class PatientRegistrationServiceImpl implements UserService {
    private UserDAO userDAO = new UserDAO();


    @Override
    public void register(User user) {
        // Add patient-specific registration logic here if needed
        userDAO.saveRecord(user);
    }
}
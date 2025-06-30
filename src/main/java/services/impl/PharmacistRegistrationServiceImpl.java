package services.impl;

import models.User;
import models.Pharmacist;
import repository.UserDAO;
import services.UserService;

public class PharmacistRegistrationServiceImpl implements UserService {
    private UserDAO userDAO = new UserDAO();

   

    @Override
    public void register(User user) {
        // Add pharmacist-specific registration logic here if needed
        userDAO.saveRecord(user);
    }
}
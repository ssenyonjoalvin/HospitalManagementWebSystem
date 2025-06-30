package services.impl;

import models.User;
import models.Doctor;
import repository.UserDAO;
import services.DoctorService;

import java.util.ArrayList;
import java.util.List;

public class DoctorServices implements DoctorService {
    private final UserDAO userDAO = new UserDAO();

    public List<Doctor> getAllDoctors() {
        List<User> allUsers = userDAO.getAllRecords();
        List<Doctor> doctors = new ArrayList<>();
        for (User user : allUsers) {
            if (user instanceof Doctor) {
                doctors.add((Doctor) user);
            }
        }
        return doctors;
    }

    public void updateDoctor(Doctor selectedDoctor) {
        userDAO.updateRecord(selectedDoctor);
    }

}
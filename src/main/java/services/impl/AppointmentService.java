package services.impl;

import models.Appointment;
import repository.AppointmentDAO;
import services.AppointmentServices;

public class AppointmentService implements AppointmentServices {
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    public void saveAppointment(Appointment appointment){
        appointmentDAO.saveAppointment(appointment);

    }
}

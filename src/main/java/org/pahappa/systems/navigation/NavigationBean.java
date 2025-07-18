package org.pahappa.systems.navigation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class NavigationBean {

    public String toDashboard() {
        return "/dashboard.xhtml?faces-redirect=true";
    }

    public String toCalendar() {
        return "/calendar.xhtml?faces-redirect=true";
    }

    public String toPatients() {
        return "/patients.xhtml?faces-redirect=true";
    }

    public String toAppointments() {
        return "/appointments.xhtml?faces-redirect=true";
    }

    public String toPayments() {
        return "/payments.xhtml?faces-redirect=true";
    }

    public String toEmployees() {
        return "/employees.xhtml?faces-redirect=true";
    }

    public String toNewAppointment() {
        return "/new-appointment.xhtml?faces-redirect=true";
    }

    public String cancelNew() {
        return "/appointments.xhtml?faces-redirect=true";
    }

    public String toEditPatient() {
        return "/edit-patient.xhtml?faces-redirect=true";

    }

    // Employees Navigation
    public String toRegisterEmployee() {
        return "/register-employee.xhtml?faces-redirect=true";
    }

    public String toRegisterPatient() {
        return "/register-patient.xhtml?faces-redirect=true";
    }
//for the  hospital services
    public String toNewService(){
        return "/add-service.xhtml?faces-redirect=true";
    }
    public String toServiceTable(){
        return "/service-display.xhtml?faces-redirect=true";
    }

}

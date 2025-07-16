package org.pahappa.systems.views;


import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import org.pahappa.systems.models.Service;
import org.pahappa.systems.repository.ServiceDAO;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named("serviceBean")
@ViewScoped
public class ServiceBean implements Serializable {
    private Service newService = new Service();

    @Inject
    private ServiceDAO serviceDAO;

    public void saveService() {
        try {
            serviceDAO.saveRecord(newService);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Service added successfully!", null));
            newService = new Service(); // Reset form
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding service: " + e.getMessage(), null));
        }
    }

    public Service getNewService() {
        return newService;
    }

    public void setNewService(Service newService) {
        this.newService = newService;
    }
}
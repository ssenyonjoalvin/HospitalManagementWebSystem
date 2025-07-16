package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import org.pahappa.systems.models.Service;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

import org.pahappa.systems.navigation.NavigationBean;
import org.pahappa.systems.services.hospitalservice.HospitalServices;

@Named("serviceBean")
@ViewScoped
public class ServiceBean implements Serializable {
    private Service newService = new Service();
    private List<Service> allServices;

    @Inject
    private HospitalServices hospitalServices;
//    private Service serviceToDelete;
    private Service selectedService;
    @Named
    @Inject
    private NavigationBean navigationBean;

    @PostConstruct
    public void init() {
        allServices = hospitalServices.getAllServices();
    }

    public String  saveService() {
        try {
            hospitalServices.addService(newService);
            navigationBean.toServiceTable();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Service added successfully!", null));
            newService = new Service(); // Reset form
                        allServices = hospitalServices.getAllServices(); // Refresh list after add
            return  navigationBean.toServiceTable();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error adding service: " + e.getMessage(), null));
        }return null;
    }

    public Service getNewService() {
        return newService;
    }

    public void setNewService(Service newService) {
        this.newService = newService;
    }

    public List<Service> getAllServices() {
        return allServices;
    }
    public void confirmDelete(Service service) {
        this.selectedService = service;
    }

    public void deleteService() {
        if (selectedService != null) {
            hospitalServices.deleteService(selectedService);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Service deleted successfully!", null));
            allServices = hospitalServices.getAllServices(); // Refresh list
            selectedService = null;
        }
    }

    public void prepareEdit(Service service) {
        // Clone the service to avoid editing the table row directly
        this.selectedService = new Service();
        this.selectedService.setId(service.getId());
        this.selectedService.setName(service.getName());
        this.selectedService.setDescription(service.getDescription());
        this.selectedService.setPrice(service.getPrice());
    }

    public void updateService() {
        try {
            hospitalServices.updateService(selectedService);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Service updated successfully!", null));
            allServices = hospitalServices.getAllServices(); // Refresh list
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating service: " + e.getMessage(), null));
        }
    }

    public void setSelectedService(Service selectedService) {
        this.selectedService = selectedService;
    }

    public Service getSelectedService() {
        return selectedService;
    }
}
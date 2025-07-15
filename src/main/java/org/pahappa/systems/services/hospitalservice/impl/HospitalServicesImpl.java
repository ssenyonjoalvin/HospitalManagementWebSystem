package org.pahappa.systems.services.hospitalservice.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.Service;
import org.pahappa.systems.repository.ServiceDAO;
import org.pahappa.systems.services.hospitalservice.HospitalServices;

import java.util.List;

@ApplicationScoped
public class HospitalServicesImpl implements HospitalServices {

    @Inject
    private ServiceDAO serviceDAO;

    @Override
    public void addService(Service service) {
        serviceDAO.saveRecord(service);
    }

    @Override
    public List<Service> getAllServices() {
        return serviceDAO.getAllRecords();
    }

    @Override
    public void deleteService(Service service) {
     serviceDAO.deleteRecord(service.getId());
    }

    @Override
    public void updateService(Service service) {
        serviceDAO.updateRecord(service);
    }
}
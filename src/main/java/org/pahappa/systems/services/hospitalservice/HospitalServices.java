package org.pahappa.systems.services.hospitalservice;

import org.pahappa.systems.models.Service;
import java.util.List;

public interface HospitalServices {
    void addService(Service service);

    List<Service> getAllServices();

    void deleteService(Service service);

    void updateService(Service service);
}
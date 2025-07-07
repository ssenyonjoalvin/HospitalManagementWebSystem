package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.pahappa.systems.models.Service;
import org.pahappa.systems.repository.ServiceDAO;

@FacesConverter(value = "serviceConverter", managed = true)
public class ServiceConverter implements Converter<Service> {
    @Inject
    private ServiceDAO serviceDAO;

    @Override
    public Service getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            Long id = Long.parseLong(value);
            return serviceDAO.getRecordById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Service service) {
        if (service == null) {
            return "";
        }
        return service.getId().toString();
    }
}
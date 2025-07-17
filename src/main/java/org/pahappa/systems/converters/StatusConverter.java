package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.StatusEntity;
import jakarta.inject.Inject;
import org.pahappa.systems.repository.StatusEntityDAO;

@FacesConverter(value = "statusConverter", managed = true)
public class StatusConverter implements Converter<StatusEntity> {

    @Inject
    private StatusEntityDAO statusDAO;

    @Override
    public StatusEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return statusDAO.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, StatusEntity value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
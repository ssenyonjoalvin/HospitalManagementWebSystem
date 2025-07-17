package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.ShiftEntity;
import jakarta.inject.Inject;
import org.pahappa.systems.repository.ShiftEntityDAO;

@FacesConverter(value = "shiftConverter", managed = true)
public class ShiftConverter implements Converter<ShiftEntity> {

    @Inject
    private ShiftEntityDAO shiftDAO;

    @Override
    public ShiftEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return shiftDAO.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ShiftEntity value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
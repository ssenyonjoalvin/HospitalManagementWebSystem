package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.SpecialtyEntity;
import jakarta.inject.Inject;
import org.pahappa.systems.repository.SpecialtyEntityDAO;

@FacesConverter(value = "specialtyConverter", managed = true)
public class SpecialtyConverter implements Converter<SpecialtyEntity> {

    @Inject
    private SpecialtyEntityDAO specialtyDAO;

    @Override
    public SpecialtyEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return specialtyDAO.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, SpecialtyEntity value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
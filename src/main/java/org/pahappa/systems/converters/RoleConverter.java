package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.Role;
import org.pahappa.systems.repository.RoleDAO;
import jakarta.inject.Inject;

@FacesConverter(value = "roleConverter", managed = true)
public class RoleConverter implements Converter<Role> {

    @Inject
    private RoleDAO roleDAO;

    @Override
    public Role getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return roleDAO.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Role value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
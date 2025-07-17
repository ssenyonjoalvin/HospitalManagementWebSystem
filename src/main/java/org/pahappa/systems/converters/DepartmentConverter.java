package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.DepartmentEntity;
import jakarta.inject.Inject;
import org.pahappa.systems.repository.DepartmentEntityDAO;

@FacesConverter(value = "departmentConverter", managed = true)
public class DepartmentConverter implements Converter<DepartmentEntity> {

    @Inject
    private DepartmentEntityDAO departmentDAO;

    @Override
    public DepartmentEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return departmentDAO.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, DepartmentEntity value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
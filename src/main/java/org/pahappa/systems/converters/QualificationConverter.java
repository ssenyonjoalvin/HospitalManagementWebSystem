package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.QualificationEntity;
import org.pahappa.systems.repository.QualificationEntityDAO;
import jakarta.inject.Inject;

@FacesConverter(value = "qualificationConverter", managed = true)
public class QualificationConverter implements Converter<QualificationEntity> {

    @Inject
    private QualificationEntityDAO qualificationDAO;

    @Override
    public QualificationEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return qualificationDAO.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, QualificationEntity value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
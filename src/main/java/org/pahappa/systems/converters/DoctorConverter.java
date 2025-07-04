package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.enterprise.inject.spi.CDI;
import org.pahappa.systems.models.Doctor;
import org.pahappa.systems.repository.UserDAO;

@FacesConverter(value = "doctorConverter", managed = true)
public class DoctorConverter implements Converter<Doctor> {

    private UserDAO getUserDAO() {
        return CDI.current().select(UserDAO.class).get();
    }

    @Override
    public Doctor getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            long id = Long.parseLong(value);
            return getUserDAO().findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Doctor doctor) {
        if (doctor == null) {
            return "";
        }
        return String.valueOf(doctor.getId());
    }
}
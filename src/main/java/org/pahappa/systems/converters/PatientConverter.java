package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.enterprise.inject.spi.CDI;
import org.pahappa.systems.models.Patient;
import org.pahappa.systems.repository.UserDAO;

@FacesConverter(value = "patientConverter", managed = true)
public class PatientConverter implements Converter<Patient> {

    private UserDAO getUserDAO() {
        return CDI.current().select(UserDAO.class).get();
    }

    @Override
    public Patient getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            int id = Integer.parseInt(value);
            return (Patient) getUserDAO().getRecordById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Patient patient) {
        if (patient == null) {
            return "";
        }
        return String.valueOf(patient.getId());
    }
}
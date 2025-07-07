package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import org.pahappa.systems.models.Medicine;
import org.pahappa.systems.repository.MedicineDAO;

@FacesConverter(value = "medicineConverter", managed = true)
public class MedicineConverter implements Converter<Medicine> {
    @Inject
    private MedicineDAO medicineDAO;

    @Override
    public Medicine getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            Long id = Long.parseLong(value);
            return medicineDAO.getRecordById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Medicine medicine) {
        if (medicine == null) {
            return "";
        }
        return medicine.getId().toString();
    }
}
package org.pahappa.systems.converters;

import jakarta.el.ELContext;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.Medicine;
import org.pahappa.systems.views.CheckupBean;
import java.util.List;

@FacesConverter(value = "medicineConverter", managed = true)
public class MedicineConverter implements Converter<Medicine> {

    @Override
    public Medicine getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            ELContext elContext = context.getELContext();
            CheckupBean checkupBean = (CheckupBean) elContext.getELResolver().getValue(elContext, null, "checkupBean");
            if (checkupBean == null) {
                return null;
            }
            List<Medicine> availableMedicines = checkupBean.getAvailableMedicines();
            if (availableMedicines == null) {
                return null;
            }
            Long id = Long.valueOf(value);
            return availableMedicines.stream()
                    .filter(med -> med.getId() != null && med.getId().equals(id))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Medicine value) {
        if (value == null || value.getId() == null)
            return "";
        return String.valueOf(value.getId());
    }
}
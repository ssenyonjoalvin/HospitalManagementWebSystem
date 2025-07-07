package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.Medicine;
import org.pahappa.systems.services.billing.BillingAndReportingService;
import jakarta.inject.Inject;

@FacesConverter(value = "medicineConverter", managed = true)
public class MedicineConverter implements Converter<Medicine> {

    @Inject
    private BillingAndReportingService billingService;

    @Override
    public Medicine getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            Long id = Long.valueOf(value);
            return billingService.getAllMedicines().stream()
                    .filter(med -> med.getId().equals(id))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Medicine value) {
        if (value == null)
            return "";
        return String.valueOf(value.getId());
    }
}
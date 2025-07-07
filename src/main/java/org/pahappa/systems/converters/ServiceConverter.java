package org.pahappa.systems.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.Service;
import org.pahappa.systems.services.billing.BillingAndReportingService;
import jakarta.inject.Inject;

@FacesConverter(value = "serviceConverter", managed = true)
public class ServiceConverter implements Converter<Service> {

    @Inject
    private BillingAndReportingService billingService;

    @Override
    public Service getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            Long id = Long.valueOf(value);
            return billingService.getAllServices().stream()
                    .filter(svc -> svc.getId().equals(id))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Service value) {
        if (value == null)
            return "";
        return String.valueOf(value.getId());
    }
}
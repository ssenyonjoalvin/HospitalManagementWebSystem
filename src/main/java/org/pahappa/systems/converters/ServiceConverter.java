package org.pahappa.systems.converters;

import jakarta.el.ELContext;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import org.pahappa.systems.models.Service;
import org.pahappa.systems.views.CheckupBean;
import java.util.List;

@FacesConverter(value = "serviceConverter", managed = true)
public class ServiceConverter implements Converter<Service> {

    @Override
    public Service getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            ELContext elContext = context.getELContext();
            CheckupBean checkupBean = (CheckupBean) elContext.getELResolver().getValue(elContext, null, "checkupBean");
            if (checkupBean == null) {
                return null;
            }
            List<Service> availableServices = checkupBean.getAvailableServices();
            if (availableServices == null) {
                return null;
            }
            Long id = Long.valueOf(value);
            return availableServices.stream()
                    .filter(svc -> svc.getId() != null && svc.getId().equals(id))
                    .findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Service value) {
        if (value == null || value.getId() == null)
            return "";
        return String.valueOf(value.getId());
    }
}
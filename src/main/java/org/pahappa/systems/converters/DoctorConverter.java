package org.pahappa.systems.converters;
/*represents JSF UI componets */
import jakarta.faces.component.UIComponent;

/*Provides context for JSF requests,including access to
 * application session and request daata
 */
import jakarta.faces.context.FacesContext;

/*Is an interface that all JSFconverters must implement 
 * it defines how to covert between a stringfrom the UI and 
 * a java object
 */
import jakarta.faces.convert.Converter;

//annotation to register this class as a JSF converter 
//so that it can be used in JSF pages
import jakarta.faces.convert.FacesConverter;

 /*Provides access to the CDI (Contexts and Dependency Injection) 
container, allowing you to programmatically obtain beans. */
import jakarta.enterprise.inject.spi.CDI;

import org.pahappa.systems.models.Doctor;//application's doctor model
import org.pahappa.systems.repository.UserDAO;// data access object 4 user related database opera

@FacesConverter(value = "doctorConverter", managed = true) //Registers this class as a JSF converter with the name "doctorConverter", 
                                                           // and allows CDI injection (managed = true).
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
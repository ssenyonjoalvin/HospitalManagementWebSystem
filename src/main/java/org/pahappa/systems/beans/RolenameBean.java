package org.pahappa.systems.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.pahappa.systems.enums.Rolename;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("rolenameBean")
@ApplicationScoped
public class RolenameBean implements Serializable {
    public List<Rolename> getRoles() {
        return Arrays.asList(Rolename.DOCTOR, Rolename.PHARMACIST, Rolename.RECEPTIONIST);
    }
}
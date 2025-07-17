package org.pahappa.systems.views;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.pahappa.systems.models.Role;
import org.pahappa.systems.services.RoleService;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("rolenameBean")
@ApplicationScoped
public class RolenameBean implements Serializable {
    @Inject
    private RoleService roleService;

    public List<Role> getRoles() {
        return roleService.getAll();
    }
}
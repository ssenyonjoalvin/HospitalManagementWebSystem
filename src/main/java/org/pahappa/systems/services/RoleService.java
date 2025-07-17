package org.pahappa.systems.services;

import org.pahappa.systems.models.Role;
import java.util.List;

public interface RoleService {
    void save(Role role);
    void update(Role role);
    Role getById(long id);
    List<Role> getAll();
    void delete(long id);
} 
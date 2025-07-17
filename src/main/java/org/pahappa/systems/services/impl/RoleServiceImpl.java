package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.Role;
import org.pahappa.systems.repository.RoleDAO;
import org.pahappa.systems.services.RoleService;

import java.util.List;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {
    @Inject
    private RoleDAO roleDAO;

    @Override
    public void save(Role role) { roleDAO.save(role); }
    @Override
    public void update(Role role) { roleDAO.update(role); }
    @Override
    public Role getById(long id) { return roleDAO.getById(id); }
    @Override
    public List<Role> getAll() { return roleDAO.getAll(); }
    @Override
    public void delete(long id) { roleDAO.delete(id); }
} 
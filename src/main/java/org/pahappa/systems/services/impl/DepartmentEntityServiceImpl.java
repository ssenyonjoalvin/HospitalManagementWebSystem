package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.DepartmentEntity;
import org.pahappa.systems.repository.DepartmentEntityDAO;
import org.pahappa.systems.services.DepartmentEntityService;

import java.util.List;

@ApplicationScoped
public class DepartmentEntityServiceImpl implements DepartmentEntityService {
    @Inject
    private DepartmentEntityDAO departmentEntityDAO;

    @Override
    public void save(DepartmentEntity entity) { departmentEntityDAO.save(entity); }
    @Override
    public void update(DepartmentEntity entity) { departmentEntityDAO.update(entity); }
    @Override
    public DepartmentEntity getById(long id) { return departmentEntityDAO.getById(id); }
    @Override
    public List<DepartmentEntity> getAll() { return departmentEntityDAO.getAll(); }
    @Override
    public void delete(long id) { departmentEntityDAO.delete(id); }
} 
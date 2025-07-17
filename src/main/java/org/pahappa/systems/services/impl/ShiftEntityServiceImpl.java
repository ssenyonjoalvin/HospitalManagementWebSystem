package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.ShiftEntity;
import org.pahappa.systems.repository.ShiftEntityDAO;
import org.pahappa.systems.services.ShiftEntityService;

import java.util.List;

@ApplicationScoped
public class ShiftEntityServiceImpl implements ShiftEntityService {
    @Inject
    private ShiftEntityDAO shiftEntityDAO;

    @Override
    public void save(ShiftEntity entity) { shiftEntityDAO.save(entity); }
    @Override
    public void update(ShiftEntity entity) { shiftEntityDAO.update(entity); }
    @Override
    public ShiftEntity getById(long id) { return shiftEntityDAO.getById(id); }
    @Override
    public List<ShiftEntity> getAll() { return shiftEntityDAO.getAll(); }
    @Override
    public void delete(long id) { shiftEntityDAO.delete(id); }
} 
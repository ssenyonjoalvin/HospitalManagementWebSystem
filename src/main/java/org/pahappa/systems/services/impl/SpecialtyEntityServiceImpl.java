package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.SpecialtyEntity;
import org.pahappa.systems.repository.SpecialtyEntityDAO;
import org.pahappa.systems.services.SpecialtyEntityService;

import java.util.List;

@ApplicationScoped
public class SpecialtyEntityServiceImpl implements SpecialtyEntityService {
    @Inject
    private SpecialtyEntityDAO specialtyEntityDAO;

    @Override
    public void save(SpecialtyEntity entity) {
        specialtyEntityDAO.save(entity);
    }

    @Override
    public void update(SpecialtyEntity entity) {
        specialtyEntityDAO.update(entity);
    }

    @Override
    public SpecialtyEntity getById(long id) {
        return specialtyEntityDAO.getById(id);
    }

    @Override
    public List<SpecialtyEntity> getAll() {
        return specialtyEntityDAO.getAll();
    }

    @Override
    public void delete(long id) {
        specialtyEntityDAO.delete(id);
    }
}
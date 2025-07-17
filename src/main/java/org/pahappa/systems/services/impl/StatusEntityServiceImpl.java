package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.StatusEntity;
import org.pahappa.systems.repository.StatusEntityDAO;
import org.pahappa.systems.services.StatusEntityService;

import java.util.List;

@ApplicationScoped
public class StatusEntityServiceImpl implements StatusEntityService {
    @Inject
    private StatusEntityDAO statusEntityDAO;

    @Override
    public void save(StatusEntity entity) {
        statusEntityDAO.save(entity);
    }

    @Override
    public void update(StatusEntity entity) {
        statusEntityDAO.update(entity);
    }

    @Override
    public StatusEntity getById(long id) {
        return statusEntityDAO.getById(id);
    }

    @Override
    public List<StatusEntity> getAll() {
        return statusEntityDAO.getAll();
    }

    @Override
    public void delete(long id) {
        statusEntityDAO.delete(id);
    }
}
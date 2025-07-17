package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.QualificationEntity;
import org.pahappa.systems.repository.QualificationEntityDAO;
import org.pahappa.systems.services.QualificationEntityService;

import java.util.List;

@ApplicationScoped
public class QualificationEntityServiceImpl implements QualificationEntityService {
    @Inject
    private QualificationEntityDAO qualificationEntityDAO;

    @Override
    public void save(QualificationEntity entity) {
        qualificationEntityDAO.save(entity);
    }

    @Override
    public void update(QualificationEntity entity) {
        qualificationEntityDAO.update(entity);
    }

    @Override
    public QualificationEntity getById(long id) {
        return qualificationEntityDAO.getById(id);
    }

    @Override
    public List<QualificationEntity> getAll() {
        return qualificationEntityDAO.getAll();
    }

    @Override
    public void delete(long id) {
        qualificationEntityDAO.delete(id);
    }
}
package org.pahappa.systems.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.PatientTypeEntity;
import org.pahappa.systems.repository.PatientTypeEntityDAO;
import org.pahappa.systems.services.PatientTypeEntityService;

import java.util.List;

@ApplicationScoped
public class PatientTypeEntityServiceImpl implements PatientTypeEntityService {
    @Inject
    private PatientTypeEntityDAO patientTypeEntityDAO;

    @Override
    public void save(PatientTypeEntity entity) {
        patientTypeEntityDAO.save(entity);
    }

    @Override
    public void update(PatientTypeEntity entity) {
        patientTypeEntityDAO.update(entity);
    }

    @Override
    public PatientTypeEntity getById(long id) {
        return patientTypeEntityDAO.getById(id);
    }

    @Override
    public List<PatientTypeEntity> getAll() {
        return patientTypeEntityDAO.getAll();
    }

    @Override
    public void delete(long id) {
        patientTypeEntityDAO.delete(id);
    }
}
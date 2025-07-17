package org.pahappa.systems.services;

import org.pahappa.systems.models.PatientTypeEntity;
import java.util.List;

public interface PatientTypeEntityService {
    void save(PatientTypeEntity entity);

    void update(PatientTypeEntity entity);

    PatientTypeEntity getById(long id);

    List<PatientTypeEntity> getAll();

    void delete(long id);
}
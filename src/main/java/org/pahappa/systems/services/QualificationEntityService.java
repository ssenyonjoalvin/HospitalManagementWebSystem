package org.pahappa.systems.services;

import org.pahappa.systems.models.QualificationEntity;
import java.util.List;

public interface QualificationEntityService {
    void save(QualificationEntity entity);

    void update(QualificationEntity entity);

    QualificationEntity getById(long id);

    List<QualificationEntity> getAll();

    void delete(long id);
}
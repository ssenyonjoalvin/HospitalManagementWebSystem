package org.pahappa.systems.services;

import org.pahappa.systems.models.DepartmentEntity;
import java.util.List;

public interface DepartmentEntityService {
    void save(DepartmentEntity entity);

    void update(DepartmentEntity entity);

    DepartmentEntity getById(long id);

    List<DepartmentEntity> getAll();

    void delete(long id);
}
package org.pahappa.systems.services;

import org.pahappa.systems.models.StatusEntity;
import java.util.List;

public interface StatusEntityService {
    void save(StatusEntity entity);
    void update(StatusEntity entity);
    StatusEntity getById(long id);
    List<StatusEntity> getAll();
    void delete(long id);
}
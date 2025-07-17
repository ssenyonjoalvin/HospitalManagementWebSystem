package org.pahappa.systems.services;

import org.pahappa.systems.models.ShiftEntity;
import java.util.List;

public interface ShiftEntityService {
    void save(ShiftEntity entity);

    void update(ShiftEntity entity);

    ShiftEntity getById(long id);

    List<ShiftEntity> getAll();

    void delete(long id);
}
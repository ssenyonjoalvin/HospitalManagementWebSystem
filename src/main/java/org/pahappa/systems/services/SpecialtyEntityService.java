package org.pahappa.systems.services;

import org.pahappa.systems.models.SpecialtyEntity;
import java.util.List;

public interface SpecialtyEntityService {
    void save(SpecialtyEntity entity);

    void update(SpecialtyEntity entity);

    SpecialtyEntity getById(long id);

    List<SpecialtyEntity> getAll();

    void delete(long id);
}
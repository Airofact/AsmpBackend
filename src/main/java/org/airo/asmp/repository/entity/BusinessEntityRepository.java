package org.airo.asmp.repository.entity;

import org.airo.asmp.model.entity.BusinessEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BusinessEntityRepository<T extends BusinessEntity> extends CrudRepository<T, UUID> {

}

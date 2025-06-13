package org.airo.asmp.repository.entity;

import org.airo.asmp.model.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessEntityRepository<T extends BusinessEntity> extends JpaRepository<T, UUID> {

}


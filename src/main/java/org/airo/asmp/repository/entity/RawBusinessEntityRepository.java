package org.airo.asmp.repository.entity;

import org.airo.asmp.model.entity.BusinessEntity;
import org.airo.asmp.util.SpecificationBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RawBusinessEntityRepository extends JpaRepository<BusinessEntity, UUID>, JpaSpecificationExecutor<BusinessEntity> {

}

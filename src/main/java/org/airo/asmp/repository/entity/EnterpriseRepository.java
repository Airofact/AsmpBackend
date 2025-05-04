package org.airo.asmp.repository.entity;

import org.airo.asmp.model.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EnterpriseRepository extends CrudRepository<Enterprise, UUID>, JpaSpecificationExecutor<Enterprise> {
}

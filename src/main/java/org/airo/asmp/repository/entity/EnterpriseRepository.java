package org.airo.asmp.repository.entity;

import org.airo.asmp.model.entity.Enterprise;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnterpriseRepository extends BusinessEntityRepository<Enterprise>, JpaSpecificationExecutor<Enterprise> {
}

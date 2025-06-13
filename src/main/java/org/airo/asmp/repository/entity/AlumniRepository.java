package org.airo.asmp.repository.entity;

import jakarta.validation.constraints.NotNull;
import org.airo.asmp.model.entity.Alumni;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AlumniRepository extends BusinessEntityRepository<Alumni>, JpaSpecificationExecutor<Alumni> {

	List<Alumni> findByRealName(@NotNull String realName);
}

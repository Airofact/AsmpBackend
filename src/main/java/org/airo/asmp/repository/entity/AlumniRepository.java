package org.airo.asmp.repository.entity;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.airo.asmp.model.Gender;
import org.airo.asmp.model.entity.Alumni;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.stream.StreamSupport;

public interface AlumniRepository extends BusinessEntityRepository<Alumni>, JpaSpecificationExecutor<Alumni> {

	List<Alumni> findByRealName(@NotNull String realName);
}

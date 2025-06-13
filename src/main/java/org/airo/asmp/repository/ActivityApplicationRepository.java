package org.airo.asmp.repository;

import org.airo.asmp.model.activity.ActivityApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ActivityApplicationRepository extends JpaRepository<ActivityApplication, UUID>, JpaSpecificationExecutor<ActivityApplication> {
}

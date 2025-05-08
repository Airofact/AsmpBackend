package org.airo.asmp.repository;

import io.swagger.v3.core.jackson.mixin.Schema31Mixin;
import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityApplicationRepository extends JpaRepository<ActivityApplication, String> {
}

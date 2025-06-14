package org.airo.asmp.repository;

import org.airo.asmp.model.activity.ActivityAlumniId;
import org.airo.asmp.model.activity.ActivityApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ActivityApplicationRepository extends JpaRepository<ActivityApplication, ActivityAlumniId>, JpaSpecificationExecutor<ActivityApplication> {
	List<ActivityApplication> findByActivityId(UUID activityId);
}

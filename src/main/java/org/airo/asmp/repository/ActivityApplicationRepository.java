package org.airo.asmp.repository;

import org.airo.asmp.model.activity.ActivityAlumniId;
import org.airo.asmp.model.activity.ActivityApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ActivityApplicationRepository extends JpaRepository<ActivityApplication, ActivityAlumniId>, JpaSpecificationExecutor<ActivityApplication> {
}

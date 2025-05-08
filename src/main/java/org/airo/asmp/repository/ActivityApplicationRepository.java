package org.airo.asmp.repository;

import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityApplicationRespository extends JpaRepository<ActivityApplication, Long> {
}

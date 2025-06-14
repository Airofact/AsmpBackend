package org.airo.asmp.repository;

import org.airo.asmp.model.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public interface ActivityRepository extends JpaRepository<Activity, UUID>, JpaSpecificationExecutor<Activity> {
    
    // 使用方法名查询，避免JPQL
    List<Activity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<Activity> findByOrganizerId(UUID organizerId);

    List<Activity> findByTitle(String title);
    
    List<Activity> findByTitleContaining(String title);

    Activity getActivitiesById(UUID id);
}

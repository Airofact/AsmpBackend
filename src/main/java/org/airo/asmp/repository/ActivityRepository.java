package org.airo.asmp.repository;

import org.airo.asmp.dto.entity.ActivityStatusCountDto;
import org.airo.asmp.model.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.data.repository.query.Param;
public interface ActivityRepository extends JpaRepository<Activity, UUID>, JpaSpecificationExecutor<Activity> {
    @Query("SELECT new org.airo.asmp.dto.entity.ActivityStatusCountDto(a.status, COUNT(a)) FROM Activity a GROUP BY a.status")
    List<ActivityStatusCountDto> countByStatusGroup();

    @Query("SELECT a FROM Activity a WHERE a.startTime >= :start AND a.endTime <= :end")
    List<Activity> findByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
           SELECT new org.airo.asmp.dto.entity.ActivityStatusCountDto(a.status, COUNT(a))
           FROM Activity a
           WHERE a.startTime >= :start AND a.endTime <= :end
           GROUP BY a.status
           """)
    List<ActivityStatusCountDto> countByStatusInTimeRange(@Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end);

    List<Activity> findByOrganizerId(UUID organizerId);

    List<Activity> findByTitle(String title);
}

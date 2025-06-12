package org.airo.asmp.repository;

import io.swagger.v3.core.jackson.mixin.Schema31Mixin;
import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ActivityApplicationRepository extends JpaRepository<ActivityApplication, UUID> {
    @Query("""
        SELECT aa FROM ActivityApplication aa
        WHERE (:realName IS NULL OR aa.alumni.realName = :realName)
          AND (:title IS NULL OR aa.activity.title = :title)
          AND (:signedIn IS NULL OR aa.signedIn = :signedIn)
    """)
    List<ActivityApplication> findByRealNameAndTitleAndSignedIn(
            @Param("realName") String realName,
            @Param("title") String title,
            @Param("signedIn") Boolean signedIn
    );
}

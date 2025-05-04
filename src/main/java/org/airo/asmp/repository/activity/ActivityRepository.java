package org.airo.asmp.repository.activity;

import org.airo.asmp.model.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}

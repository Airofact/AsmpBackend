package org.airo.asmp.repository.entity;

import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.airo.asmp.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    List<Organization> findByName(String name);
}

package org.airo.asmp.model.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;
@Data
@Embeddable
public class OrganizationAlumniId implements Serializable {
    private UUID organizationId;
    private UUID alumniId;
}

package org.airo.asmp.model.OrganizationMember;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
@Embeddable
@Getter
@Setter
public class OrganizationAlumniId implements Serializable {
    private UUID organizationId;
    private UUID alumniId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrganizationAlumniId)) return false;
        OrganizationAlumniId that = (OrganizationAlumniId) o;
        return Objects.equals(organizationId, that.organizationId) &&
                Objects.equals(alumniId, that.alumniId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, alumniId);
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public UUID getAlumniId() {
        return alumniId;
    }

    public void setAlumniId(UUID alumniId) {
        this.alumniId = alumniId;
    }
}

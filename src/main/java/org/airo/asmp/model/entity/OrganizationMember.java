package org.airo.asmp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrganizationMember {
    @EmbeddedId
    private OrganizationAlumniId id;

    @ManyToOne
    @MapsId("organizationId")
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @MapsId("alumniId")
    @JoinColumn(name = "alumni_id", nullable = false)
    private Alumni alumni;

    @Column()
    LocalDateTime joinTime;

    @Column()
	Role role;

    public enum Role {
        CREATOR,
        MEMBER,
        ADMIN
    }
}

package org.airo.asmp.model.OrganizationMember;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.entity.Organization;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrganizationMember {
    @EmbeddedId
    @JsonIgnore
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
}

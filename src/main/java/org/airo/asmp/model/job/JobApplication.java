package org.airo.asmp.model.job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.airo.asmp.model.entity.Alumni;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class JobApplication {
    @Id
    @UuidGenerator
    UUID id; 

    @ManyToOne
    @JoinColumn(name = "job_post_id")
    JobPost jobPost; 

    @ManyToOne
    @JoinColumn(name = "alumni_id")
    private Alumni alumni; 

    @Column
    LocalDateTime applyTime; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ApplicationStatus status = ApplicationStatus.PENDING;

    public enum ApplicationStatus {
        PENDING,
        TESTED,
        INTERVIEWED,
        HIRED,
        REJECTED,
    }
}
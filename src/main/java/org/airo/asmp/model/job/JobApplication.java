package org.airo.asmp.model.job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.job.JobPost;
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
    @JoinColumn(name = "jobPostId")
    JobPost jobPost; 

    @ManyToOne
    @JoinColumn(name = "alumniId")
    private Alumni alumni; 

    @Column
    LocalDateTime applyTime; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ApplicationStatus status = ApplicationStatus.pending;
}
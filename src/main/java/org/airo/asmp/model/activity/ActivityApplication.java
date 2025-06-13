package org.airo.asmp.model.activity;

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
public class ActivityApplication{
    @EmbeddedId
    ActivityAlumniId id;

    @ManyToOne
    @MapsId("activityId")
    @JoinColumn(name = "activity_id", nullable = false)
    Activity activity;

    @ManyToOne
    @MapsId("alumniId")
    @JoinColumn(name = "alumni_id", nullable = false)
    Alumni alumni;

    @Column()
    LocalDateTime applyTime;

    @Column(columnDefinition = "bool")
    boolean signedIn=false;
}

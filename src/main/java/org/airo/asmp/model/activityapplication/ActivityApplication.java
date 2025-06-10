package org.airo.asmp.model.activityapplication;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.entity.Alumni;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ActivityApplication{
    @Id
    @UuidGenerator
    UUID id;


    @OneToOne
    @JoinColumn(name = "activityId", nullable = false)
    Activity activity;

    @ManyToOne
    @JoinColumn(name = "alumniId", nullable = false)
    Alumni alumni;

    @Column()
    LocalDateTime applyTime;

    @Column(columnDefinition = "bool")
    boolean signedIn=false;
}

package org.airo.asmp.model.activity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.airo.asmp.model.entity.Organization;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Entity
public class Activity {
    @Id
    @UuidGenerator
    UUID id;

  @Column(columnDefinition = "varchar(100)")
  String title;

  @Column(columnDefinition = "text")
  String description;

  @Column()
  LocalDateTime startTime;

  @Column()
  LocalDateTime endTime;

  @Column(columnDefinition = "varchar(200)")
  String location;

  @Column(columnDefinition = "int")
  int maxParticipants;

  @ManyToOne
  @JoinColumn(name="organizerId")
    Organization organizer;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  Status status=Status.not_started;
}

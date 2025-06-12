package org.airo.asmp.model.job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.airo.asmp.model.entity.Enterprise;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class JobPost {
    @Id
    @UuidGenerator
    UUID id; 

    @Column(columnDefinition = "varchar(100)", nullable = false)
    String title; 

    @Column(columnDefinition = "varchar(100)", nullable = false)
    String jobType; 

    @Column(columnDefinition = "text")
    String description; 

    @Column(columnDefinition = "int")
    int salaryMin; 

    @Column(columnDefinition = "int")
    int salaryMax;

    @Column()
    LocalDateTime publishTime; 

    @ManyToOne
    @JoinColumn(name = "enterpriseId")
    private Enterprise enterprise; 
}
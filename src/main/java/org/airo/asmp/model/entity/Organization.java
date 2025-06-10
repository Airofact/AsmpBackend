package org.airo.asmp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Organization extends BusinessEntity{

    @Column(columnDefinition = "varchar(100)")
    String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Type type =Type.interest;

    @Column(columnDefinition = "text")
    String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creatorId", nullable = false)
    Alumni alumni;
}

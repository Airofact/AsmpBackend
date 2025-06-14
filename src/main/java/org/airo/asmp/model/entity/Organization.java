package org.airo.asmp.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Organization extends BusinessEntity{

    @Column(columnDefinition = "varchar(100)")
    String name;

    @Column(nullable = false)
    Type type = Type.INTEREST;

    @Column(columnDefinition = "text")
    String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    Alumni creator;

    @Column(nullable = false)
    State state =State.ACTIVE;

    public enum Type {
        REGIONAL,
        INDUSTRIAL,
        INTEREST
    }
    public enum State{
        DISBAND,
        ACTIVE
    }
}

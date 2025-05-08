package org.airo.asmp.model.notice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
public class Notice {
    @Id
    @Column(columnDefinition = "char(36)")
    String id=UUID.randomUUID().toString();

    @Column(columnDefinition = "varchar(100)")
    String title;

    @Column(columnDefinition = "text")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Type type;

}



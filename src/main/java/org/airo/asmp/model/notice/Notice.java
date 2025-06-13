package org.airo.asmp.model.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Setter
@Getter
public class Notice {
    @Id
    @UuidGenerator
    @JsonIgnore
    UUID id;

    @Column(columnDefinition = "varchar(100)")
    String title;

    @Column(columnDefinition = "text")
    String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Type type;

}



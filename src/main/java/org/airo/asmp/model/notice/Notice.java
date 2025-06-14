package org.airo.asmp.model.notice;

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
    UUID id;

    @Column(columnDefinition = "varchar(100)")
    String title;

    @Column(columnDefinition = "text")
    String content;

    @Column(nullable = false)
    Type type;

	public enum Type {
		NEWS,
		ANNOUNCEMENT,
		NOTICE
	}
}



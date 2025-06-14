package org.airo.asmp.model.activity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.airo.asmp.model.entity.Organization;
import org.hibernate.annotations.UuidGenerator;

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
	@JoinColumn(name = "organizer_id")
	Organization organizer;

	// status字段改为计算列，根据时间动态确定状态
	@Transient  // 不存储在数据库中
	public Status getStatus() {
		LocalDateTime now = LocalDateTime.now();
		if (now.isBefore(startTime)) {
			return Status.NOT_STARTED;
		} else if (now.isAfter(endTime)) {
			return Status.FINISHED;
		} else {
			return Status.STARTED;
		}
	}
}

package org.airo.asmp.model.activity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class ActivityAlumniId implements Serializable {
	private UUID activityId;
	private UUID alumniId;
}

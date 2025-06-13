package org.airo.asmp.dto.activity;

import org.airo.asmp.model.activity.ActivityApplication;

import java.util.UUID;

/**
 * DTO for updating {@link ActivityApplication}
 */
public record ActivityApplicationUpdateDto(
        UUID activityId,
        UUID alumniId,
        boolean signedIn
) {}

package org.airo.asmp.dto.activity;

import org.airo.asmp.model.activity.ActivityApplication;

/**
 * DTO for updating {@link ActivityApplication}
 * Only mutable fields can be updated
 */
public record ActivityApplicationUpdateDto(
        Boolean signedIn
) {}

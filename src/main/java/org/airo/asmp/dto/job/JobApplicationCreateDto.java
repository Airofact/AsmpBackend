package org.airo.asmp.dto.job;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link org.airo.asmp.model.job.JobApplication}
 * @param alumniId
 * @param applyTime
 */

public record JobApplicationCreateDto(
        UUID alumniId,        
        LocalDateTime applyTime
) {
}
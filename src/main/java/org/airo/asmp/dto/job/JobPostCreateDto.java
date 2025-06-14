package org.airo.asmp.dto.job;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link org.airo.asmp.model.job.JobPost}
 *
 * @param title The title of the job post.
 * @param description A description of the job post.
 * @param jobType The type of job (e.g., full-time, part-time).
 * @param salaryMin The minimum salary for the job.
 * @param salaryMax The maximum salary for the job.
 * @param enterpriseId The ID of the enterprise posting the job.
 */

public record JobPostCreateDto(
		@NotNull String title,
        String description,
		@NotNull String jobType,
		@NotNull Integer salaryMin,
		@NotNull Integer salaryMax,
        @NotNull UUID enterpriseId     
) implements Serializable {
}
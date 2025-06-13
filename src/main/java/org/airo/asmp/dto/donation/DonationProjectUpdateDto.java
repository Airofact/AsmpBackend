package org.airo.asmp.dto.donation;

import org.airo.asmp.model.donation.DonationProject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for updating {@link DonationProject}
 */
public record DonationProjectUpdateDto(
        String name,
        String description,
        BigDecimal targetAmount,
        LocalDateTime startDate,
        LocalDateTime endDate,
        DonationProject.ProjectStatus status,
        String category,
        String imageUrl,
        UUID organizerId
) {}

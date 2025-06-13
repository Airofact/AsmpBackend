package org.airo.asmp.dto.donation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.airo.asmp.model.donation.DonationProject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for creating {@link DonationProject}
 */
public record DonationProjectCreateDto(
        @NotBlank String name,
        String description,
        @NotNull @Positive BigDecimal targetAmount,
        @NotNull LocalDateTime startDate,
        LocalDateTime endDate,
        String category,
        String imageUrl,
        UUID organizerId  // 项目发起者ID，可以是组织或其他BusinessEntity
) {}

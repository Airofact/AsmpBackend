package org.airo.asmp.dto.donation;

import org.airo.asmp.dto.entity.BusinessEntityFilterDto;
import org.airo.asmp.model.donation.DonationProject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for filtering {@link DonationProject}
 */
public record DonationProjectFilterDto(
        String name,
        String description,
        BigDecimal minTargetAmount,
        BigDecimal maxTargetAmount,
        BigDecimal minCurrentAmount,
        BigDecimal maxCurrentAmount,
        LocalDateTime startDateFrom,
        LocalDateTime startDateTo,
        LocalDateTime endDateFrom,
        LocalDateTime endDateTo,
        DonationProject.ProjectStatus status,
        String category,
		BusinessEntityFilterDto organizerFilter,
        Boolean targetReached,
        Double minProgress,
        Double maxProgress
) {}

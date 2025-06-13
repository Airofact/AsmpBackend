package org.airo.asmp.dto.donation;

import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.model.donation.Donation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for filtering {@link Donation}
 */
public record DonationFilterDto(
		AlumniFilterDto donorFilter,
		DonationProjectFilterDto donationProjectFilter,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        Donation.PaymentMethod paymentMethod,
        LocalDateTime donateTimeFrom,
        LocalDateTime donateTimeTo,
        Donation.DonationStatus status,
        Boolean anonymous,
        String transactionId,
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo
) {}

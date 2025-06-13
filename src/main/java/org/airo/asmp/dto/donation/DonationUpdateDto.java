package org.airo.asmp.dto.donation;

import org.airo.asmp.model.donation.Donation;

import java.math.BigDecimal;

/**
 * DTO for updating {@link Donation}
 */
public record DonationUpdateDto(
        BigDecimal amount,
        Donation.PaymentMethod paymentMethod,
        String remark,
        Donation.DonationStatus status,
        Boolean anonymous,
        String transactionId
) {}

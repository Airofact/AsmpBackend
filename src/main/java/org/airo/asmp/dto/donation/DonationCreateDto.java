package org.airo.asmp.dto.donation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.airo.asmp.model.donation.Donation;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for creating {@link Donation}
 */
public record DonationCreateDto(
        @NotNull UUID donorId,
        @NotNull @Positive BigDecimal amount,
        @NotNull Donation.PaymentMethod paymentMethod,
        String remark,
        Boolean anonymous,
        String transactionId
) {}

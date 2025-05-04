package org.airo.asmp.dto.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnterpriseFilterDto(
		UUID id,
		UUID addedById,
		String name,
		String field,
		String address,
		String contactPerson,
		String contactEmail,
		String contactPhone,
		LocalDateTime addedAtBegin,
		LocalDateTime addedAtEnd
) {}
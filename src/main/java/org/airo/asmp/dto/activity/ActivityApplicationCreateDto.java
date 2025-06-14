package org.airo.asmp.dto.activity;

import java.util.UUID;

/**
 * DTO for creating ActivityApplication
 * activityId will be provided via path parameter
 */
public record ActivityApplicationCreateDto(UUID alumniId){}

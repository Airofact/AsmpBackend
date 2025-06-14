package org.airo.asmp.dto.activity;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for creating ActivityApplication
 * activityId will be provided via path parameter
 */
public record ActivityApplicationCreateDto(@NotNull UUID alumniId){}

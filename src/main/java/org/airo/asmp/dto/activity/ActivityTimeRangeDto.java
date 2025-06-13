package org.airo.asmp.dto.activity;

import java.time.LocalDateTime;

public record ActivityTimeRangeDto(LocalDateTime start, LocalDateTime end) {
}

package org.airo.asmp.dto.entity;

import org.airo.asmp.model.activity.Status;

public record ActivityStatusCountDto(Status status, long count) {
}

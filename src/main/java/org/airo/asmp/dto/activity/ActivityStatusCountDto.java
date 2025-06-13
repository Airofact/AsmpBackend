package org.airo.asmp.dto.activity;

import org.airo.asmp.model.activity.Status;

public record ActivityStatusCountDto(Status status, long count) {
}

package org.airo.asmp.dto.entity;

import org.airo.asmp.model.activity.Status;
import org.airo.asmp.model.entity.Organization;

import java.time.LocalDateTime;

public record ActivityUpdateDto(String title,
                                String description,
                                LocalDateTime startTime,
                                LocalDateTime endTime,
                                String location,
                                int maxParticipants,
                                Status status) {
}

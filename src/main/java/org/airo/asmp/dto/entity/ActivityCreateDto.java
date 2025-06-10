package org.airo.asmp.dto.entity;

import jakarta.persistence.*;
import org.airo.asmp.model.activity.Status;
import org.airo.asmp.model.entity.Organization;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityCreateDto(String title,
                                String description,
                                LocalDateTime startTime,
                                LocalDateTime endTime,
                                String location,
                                int maxParticipants,
                                UUID organizer,
                                Status status){ }

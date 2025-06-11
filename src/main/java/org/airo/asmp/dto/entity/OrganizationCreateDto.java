package org.airo.asmp.dto.entity;

import lombok.Data;
import org.airo.asmp.model.entity.Alumni;

import java.util.UUID;

public record OrganizationCreateDto(String name,
        String type,
        String description,
        UUID addedById,
        UUID alumni) {}

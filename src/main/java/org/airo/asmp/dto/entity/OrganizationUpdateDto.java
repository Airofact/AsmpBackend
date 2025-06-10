package org.airo.asmp.dto.entity;

import java.util.UUID;

public record OrganizationUpdateDto(String name,
                                    String type,
                                    String description,
                                    UUID addedById) {}

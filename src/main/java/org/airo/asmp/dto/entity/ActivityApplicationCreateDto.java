package org.airo.asmp.dto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.entity.Alumni;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityApplicationCreateDto(UUID activity, UUID alumni){}

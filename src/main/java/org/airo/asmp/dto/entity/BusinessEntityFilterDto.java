package org.airo.asmp.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for filtering {@link org.airo.asmp.model.entity.BusinessEntity}
 * 通用的业务实体过滤器，可用于查询所有继承自 BusinessEntity 的实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessEntityFilterDto {
    public UUID id;
    public UUID addedById;
    public LocalDateTime addedAtBegin;
    public LocalDateTime addedAtEnd;
}

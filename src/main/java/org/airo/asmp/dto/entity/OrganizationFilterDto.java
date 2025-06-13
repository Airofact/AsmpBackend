package org.airo.asmp.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.airo.asmp.model.entity.Organization;

/**
 * DTO for filtering {@link org.airo.asmp.model.entity.Organization}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrganizationFilterDto extends BusinessEntityFilterDto {
        private String name;
        private Organization.Type type;
        private String description;
        private AlumniFilterDto creatorFilter;
}

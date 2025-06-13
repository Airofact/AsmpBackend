package org.airo.asmp.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for filtering {@link org.airo.asmp.model.entity.Enterprise}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EnterpriseFilterDto extends BusinessEntityFilterDto {
		private String name;
		private String field;
		private String address;
		private String contactPerson;
		private String contactEmail;
		private String contactPhone;
}
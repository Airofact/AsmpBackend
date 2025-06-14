package org.airo.asmp.dto.job;

import org.airo.asmp.dto.entity.EnterpriseFilterDto;

import java.time.LocalDateTime;

/**
 * 职位筛选DTO
 */
public record JobPostFilterDto(
        String title,                  
        Integer salaryMinLowerBound,
		Integer salaryMinUpperBound,
		Integer salaryMaxLowerBound,
		Integer salaryMaxUpperBound,
        String jobType,
        String description,                            
        EnterpriseFilterDto enterpriseFilter,
        LocalDateTime publishTimeStart,
		LocalDateTime publishTimeEnd
) {
}
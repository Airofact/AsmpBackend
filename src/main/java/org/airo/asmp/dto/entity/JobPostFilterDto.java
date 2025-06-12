package org.airo.asmp.dto.entity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 职位筛选DTO
 */
public record JobPostFilterDto(
        UUID id,                        
        String title,                  
        int salaryMin,            
        int salaryMax,              
        String jobType,
        String description,                            
        UUID enterpriseId,         
        LocalDateTime publishTime     
) {
}
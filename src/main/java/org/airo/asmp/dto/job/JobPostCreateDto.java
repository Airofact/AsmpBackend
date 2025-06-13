package org.airo.asmp.dto.job;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


public record JobPostCreateDto(
        String title,           
        String description,             
        String jobType,
        int salaryMin,    
        int salaryMax,              
        LocalDateTime publishTime,                                  
        @NotNull UUID enterpriseId     
) implements Serializable {
}
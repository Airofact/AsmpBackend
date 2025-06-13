package org.airo.asmp.dto.job;

import java.io.Serializable;
import java.time.LocalDateTime;

public record JobPostUpdateDto(
        String title,           
        String description,             
        String jobType,
        int salaryMin,    
        int salaryMax,              
        LocalDateTime publishTime                                 
) implements Serializable {
}
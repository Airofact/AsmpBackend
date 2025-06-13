package org.airo.asmp.dto.job;

import java.time.LocalDateTime;

import org.airo.asmp.model.job.ApplicationStatus;


public record JobApplicationUpdateDto(
        LocalDateTime applyTime,            
        ApplicationStatus status 
){
}
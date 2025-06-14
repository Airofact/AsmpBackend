package org.airo.asmp.dto.job;

import java.time.LocalDateTime;

import org.airo.asmp.model.job.JobApplication;


public record JobApplicationUpdateDto(
        LocalDateTime applyTime,            
        JobApplication.ApplicationStatus status
){
}
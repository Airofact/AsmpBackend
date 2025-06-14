package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.job.JobApplicationCreateDto;
import org.airo.asmp.dto.job.JobApplicationFilterDto;
import org.airo.asmp.dto.job.JobApplicationUpdateDto;
import org.airo.asmp.mapper.entity.JobApplicationMapper;
import org.airo.asmp.model.job.JobApplication;
import org.airo.asmp.repository.JobApplicationRepository;
import org.airo.asmp.repository.JobPostRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.airo.asmp.service.JobApplicationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job/{jobId}/application")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final AlumniRepository alumniRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<JobApplication> createJobApplication(
            @PathVariable UUID jobId,
            @RequestBody JobApplicationCreateDto dto
    ) {

            // 检查是否已经申请过
            var existing = jobApplicationRepository.findById(jobId)
                    .stream()
                    .filter(ja ->
                            ja.getAlumni().getId().equals(dto.alumniId())&&
                            ja.getStatus() != JobApplication.ApplicationStatus.REJECTED)
                    .toList();
            if (!existing.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(existing.get(0)); // 返回已存在的申请
            }

            var alumni = alumniRepository.findById(dto.alumniId());
            if (alumni.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            var jobPost = jobPostRepository.findById(jobId);
            if (jobPost.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            JobApplication jobApplication = jobApplicationMapper.toEntity(dto);
            jobApplication.setAlumni(alumni.get());
            jobApplication.setJobPost(jobPost.get());
            jobApplication.setApplyTime(LocalDateTime.now());
            jobApplication.setStatus(JobApplication.ApplicationStatus.PENDING);
            JobApplication savedApplication = jobApplicationRepository.save(jobApplication);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
    }

    @PutMapping("/{appId}")
    public ResponseEntity<JobApplication> updateJobApplication(@PathVariable UUID jobId,
                                                       @PathVariable UUID appId, 
                                                       @RequestBody JobApplicationUpdateDto dto) {
        var jobApplication = jobApplicationRepository.findById(appId);
        if (jobApplication.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        JobApplication existing = jobApplication.get();
        // 验证申请属于指定的工作
        if (!existing.getJobPost().getId().equals(jobId)) {
            return ResponseEntity.badRequest().build();
        }
        
        jobApplicationMapper.partialUpdate(dto, existing);
        JobApplication updatedApplication = jobApplicationRepository.save(existing);
        return ResponseEntity.ok(updatedApplication);
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable UUID jobId, @PathVariable UUID appId) {
        var jobApplication = jobApplicationRepository.findById(appId);
        if (jobApplication.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        JobApplication existing = jobApplication.get();
        // 验证申请属于指定的工作
        if (!existing.getJobPost().getId().equals(jobId)) {
            return ResponseEntity.badRequest().build();
        }
        

            jobApplicationRepository.deleteById(appId);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{appId}")
    public ResponseEntity<JobApplication> getJobApplication(@PathVariable UUID jobId, @PathVariable UUID appId) {
        var jobApplication = jobApplicationRepository.findById(appId);
        if (jobApplication.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        JobApplication existing = jobApplication.get();
        // 验证申请属于指定的工作
        if (!existing.getJobPost().getId().equals(jobId)) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(existing);
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getJobApplications(@PathVariable UUID jobId) {
        List<JobApplication> applications = jobApplicationRepository.findByJobPostId(jobId);
        return ResponseEntity.ok(applications);
    }    @PostMapping("/filter")
    public List<JobApplication> filterJobApplications(@PathVariable UUID jobId, 
                                                     @RequestBody JobApplicationFilterDto filterDto) {
        // 获取该工作的所有申请，然后应用过滤条件
        return jobApplicationService.findByFilter(filterDto)
                .stream()
                .filter(app -> app.getJobPost().getId().equals(jobId))
                .toList();
    }    @GetMapping("/count")
    public ResponseEntity<Long> getApplicationCount(@PathVariable UUID jobId) {
        Long count = jobApplicationRepository.countByJobPostId(jobId);
        return ResponseEntity.ok(count);
    }
}
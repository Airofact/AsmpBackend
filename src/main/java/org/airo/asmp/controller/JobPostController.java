package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.job.JobApplicationFilterDto;
import org.airo.asmp.dto.job.JobPostCreateDto;
import org.airo.asmp.dto.job.JobPostFilterDto;
import org.airo.asmp.dto.job.JobPostUpdateDto;
import org.airo.asmp.mapper.entity.JobPostMapper;
import org.airo.asmp.model.job.JobApplication;
import org.airo.asmp.model.job.JobPost;
import org.airo.asmp.repository.JobPostRepository;
import org.airo.asmp.service.JobApplicationService;
import org.airo.asmp.service.JobPostService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobPostController {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    private final JobPostService jobPostService;
    private final JobApplicationService jobApplicationService;    @PostMapping
    public ResponseEntity<JobPost> createJobPost(@RequestBody JobPostCreateDto dto) {
        JobPost jobPost = jobPostMapper.toEntity(dto);
        jobPost.setPublishTime(LocalDateTime.now());
        JobPost savedJobPost = jobPostRepository.save(jobPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJobPost);
    }    @PutMapping("/{id}")
    public ResponseEntity<JobPost> updateJobPost(@PathVariable UUID id, @RequestBody JobPostUpdateDto dto) {
        var jobPost = jobPostRepository.findById(id);
        if (jobPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        JobPost existingJobPost = jobPost.get();
        jobPostMapper.partialUpdate(dto, existingJobPost);
        JobPost updatedJobPost = jobPostRepository.save(existingJobPost);
        return ResponseEntity.ok(updatedJobPost);
    }    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable UUID id) {
        if (!jobPostRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

            jobPostRepository.deleteById(id);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPost> getJobPost(@PathVariable UUID id) {
        var jobPost = jobPostRepository.findById(id);
        return jobPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<JobPost> getJobPosts() {
        return jobPostRepository.findAll();
    }

    @PostMapping("/filter")
    public List<JobPost> filterJobPosts(@RequestBody JobPostFilterDto filterDto) {
        return jobPostService.findByFilter(filterDto);
    }

    @GetMapping("/by-enterprise/{enterpriseId}")
    public List<JobPost> getJobPostsByEnterprise(@PathVariable UUID enterpriseId) {
        return jobPostRepository.findByEnterpriseId(enterpriseId);
    }    @GetMapping("/by-job-type")
    public List<JobPost> getJobPostsByType(@RequestParam String jobType) {
        return jobPostRepository.findByJobType(jobType);
    }
    
    // ===== 全局职位申请相关端点 =====
    
    /**
     * 获取所有职位申请（不限特定职位）
     */
    @GetMapping("/application")
    public ResponseEntity<List<JobApplication>> getAllApplications() {
        List<JobApplication> applications = jobApplicationService.findAll();
        return ResponseEntity.ok(applications);
    }
    
    /**
     * 职位申请过滤查询（不限特定职位）
     */
    @PostMapping("/application/filter")
    public ResponseEntity<List<JobApplication>> filterApplications(
            @RequestBody JobApplicationFilterDto filterDto) {
        List<JobApplication> applications = jobApplicationService.findByFilter(filterDto);
        return ResponseEntity.ok(applications);
    }
}
package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.job.JobApplicationCreateDto;
import org.airo.asmp.dto.job.JobApplicationFilterDto;
import org.airo.asmp.dto.job.JobApplicationUpdateDto;
import org.airo.asmp.mapper.entity.JobApplicationMapper;
import org.airo.asmp.model.job.ApplicationStatus;
import org.airo.asmp.model.job.JobApplication;
import org.airo.asmp.repository.JobApplicationRepository;
import org.airo.asmp.repository.JobPostRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
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
    private final JobApplicationService jobApplicationService;    @PostMapping
    public ResponseEntity<String> createJobApplication(@PathVariable UUID jobId,
                                                      @RequestBody JobApplicationCreateDto dto) {
        // 验证路径参数与DTO中的jobPostId一致
        if (!jobId.equals(dto.jobPostId())) {
            return ResponseEntity.badRequest().body("路径中的工作ID与请求体中的工作ID不匹配");
        }
        
        // 检查是否已经申请过
        var existing = jobApplicationRepository.findByJobPostIdAndAlumniId(dto.jobPostId(), dto.alumniId());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("您已经申请过该职位");
        }

        var alumni = alumniRepository.findById(dto.alumniId());
        if (alumni.isEmpty()) {
            return ResponseEntity.badRequest().body("Alumni ID 不存在");
        }

        var jobPost = jobPostRepository.findById(dto.jobPostId());
        if (jobPost.isEmpty()) {
            return ResponseEntity.badRequest().body("Job Post ID 不存在");
        }

        JobApplication jobApplication = jobApplicationMapper.toEntity(dto);
        jobApplication.setAlumni(alumni.get());
        jobApplication.setJobPost(jobPost.get());
        jobApplication.setApplyTime(LocalDateTime.now());
        jobApplication.setStatus(ApplicationStatus.pending);
        jobApplicationRepository.save(jobApplication);
        return ResponseEntity.ok("申请提交成功");
    }    @PutMapping("/{appId}")
    public ResponseEntity<String> updateJobApplication(@PathVariable UUID jobId,
                                                       @PathVariable UUID appId, 
                                                       @RequestBody JobApplicationUpdateDto dto) {
        var jobApplication = jobApplicationRepository.findById(appId);
        if (jobApplication.isEmpty()) {
            return ResponseEntity.badRequest().body("申请记录不存在");
        }
        
        JobApplication existing = jobApplication.get();
        // 验证申请属于指定的工作
        if (!existing.getJobPost().getId().equals(jobId)) {
            return ResponseEntity.badRequest().body("申请记录不属于指定的工作");
        }
        
        jobApplicationMapper.partialUpdate(dto, existing);
        jobApplicationRepository.save(existing);
        return ResponseEntity.ok("申请状态更新成功");
    }    @DeleteMapping("/{appId}")
    public ResponseEntity<String> deleteJobApplication(@PathVariable UUID jobId, @PathVariable UUID appId) {
        var jobApplication = jobApplicationRepository.findById(appId);
        if (jobApplication.isEmpty()) {
            return ResponseEntity.badRequest().body("申请记录不存在");
        }
        
        JobApplication existing = jobApplication.get();
        // 验证申请属于指定的工作
        if (!existing.getJobPost().getId().equals(jobId)) {
            return ResponseEntity.badRequest().body("申请记录不属于指定的工作");
        }
        
        jobApplicationRepository.deleteById(appId);
        return ResponseEntity.ok("删除成功");
    }    @GetMapping("/{appId}")
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
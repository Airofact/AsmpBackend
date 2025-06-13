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
import org.airo.asmp.service.FilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job/apply")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final FilterService filterService;
    private final AlumniRepository alumniRepository;
    private final JobPostRepository jobPostRepository;

    @PostMapping
    public ResponseEntity<String> createJobApplication(@RequestBody JobApplicationCreateDto dto) {
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
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJobApplication(@PathVariable UUID id, @RequestBody JobApplicationUpdateDto dto) {
        var jobApplication = jobApplicationRepository.findById(id);
        if (jobApplication.isEmpty()) {
            return ResponseEntity.badRequest().body("申请记录不存在");
        }

        JobApplication existing = jobApplication.get();
        jobApplicationMapper.partialUpdate(dto, existing);
        jobApplicationRepository.save(existing);
        return ResponseEntity.ok("申请状态更新成功");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobApplication(@PathVariable UUID id) {
        if (jobApplicationRepository.existsById(id)) {
            jobApplicationRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        return ResponseEntity.badRequest().body("申请记录不存在");
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getJobApplication(@PathVariable UUID id) {
        var jobApplication = jobApplicationRepository.findById(id);
        return jobApplication.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllJobApplications() {
        return ResponseEntity.ok(jobApplicationRepository.findAll());
    }

    @PostMapping("/filter")
    public List<JobApplication> filterJobApplications(@RequestBody JobApplicationFilterDto filterDto) {
        return filterService.filterJobApplication(filterDto);
    }

    @GetMapping("/by-jobpost/{jobPostId}")
    public List<JobApplication> getApplicationsByJobPost(@PathVariable UUID jobPostId) {
        return jobApplicationRepository.findByJobPostId(jobPostId);
    }

    @GetMapping("/by-alumni/{alumniId}")
    public List<JobApplication> getApplicationsByAlumni(@PathVariable UUID alumniId) {
        return jobApplicationRepository.findByAlumniId(alumniId);
    }

    @GetMapping("/by-status")
    public List<JobApplication> getApplicationsByStatus(@RequestParam ApplicationStatus status) {
        return jobApplicationRepository.findByStatus(status);
    }

    @GetMapping("/count/{jobPostId}")
    public ResponseEntity<Long> getApplicationCount(@PathVariable UUID jobPostId) {
        Long count = jobApplicationRepository.countByJobPostId(jobPostId);
        return ResponseEntity.ok(count);
    }
}
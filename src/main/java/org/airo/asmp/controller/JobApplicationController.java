package org.airo.asmp.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.JobApplicationCreateDto;
import org.airo.asmp.dto.entity.JobApplicationFilterDto;
import org.airo.asmp.dto.entity.JobApplicationUpdateDto;
import org.airo.asmp.mapper.entity.JobApplicationMapper;
import org.airo.asmp.model.job.ApplicationStatus;
import org.airo.asmp.model.job.JobApplication;
import org.airo.asmp.repository.JobApplicationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobapplication")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;

    @PostMapping("/add")
    public ResponseEntity<String> createJobApplication(@RequestBody JobApplicationCreateDto dto) {
        // 检查是否已经申请过
        var existing = jobApplicationRepository.findByJobPostIdAndAlumniId(dto.jobPostId(), dto.alumniId());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("您已经申请过该职位");
        }

        JobApplication jobApplication = jobApplicationMapper.toEntity(dto);
        jobApplication.setApplyTime(LocalDateTime.now());
        jobApplication.setStatus(ApplicationStatus.pending);
        jobApplicationRepository.save(jobApplication);
        return ResponseEntity.ok("申请提交成功");
    }

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobApplication(@PathVariable UUID id) {
        if (jobApplicationRepository.existsById(id)) {
            jobApplicationRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        return ResponseEntity.badRequest().body("申请记录不存在");
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<JobApplication> getJobApplication(@PathVariable UUID id) {
        var jobApplication = jobApplicationRepository.findById(id);
        return jobApplication.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public List<JobApplication> filterJobApplications(@RequestBody JobApplicationFilterDto filterDto) {
        return jobApplicationRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.id() != null) {
                predicates.add(builder.equal(root.get("id"), filterDto.id()));
            }
            if (filterDto.jobPostId() != null) {
                predicates.add(builder.equal(root.get("jobPost").get("id"), filterDto.jobPostId()));
            }
            if (filterDto.alumniId() != null) {
                predicates.add(builder.equal(root.get("alumni").get("id"), filterDto.alumniId()));
            }
            if (filterDto.status() != null) {
                predicates.add(builder.equal(root.get("status"), filterDto.status()));
            }
            if (filterDto.applyTime() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("applyTime"), filterDto.applyTime()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
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
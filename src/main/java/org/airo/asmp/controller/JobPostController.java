package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.job.JobPostCreateDto;
import org.airo.asmp.dto.job.JobPostFilterDto;
import org.airo.asmp.dto.job.JobPostUpdateDto;
import org.airo.asmp.mapper.entity.JobPostMapper;
import org.airo.asmp.model.job.JobPost;
import org.airo.asmp.repository.JobPostRepository;
import org.airo.asmp.service.FilterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job/post")
@RequiredArgsConstructor
public class JobPostController {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    private final FilterService filterService;

    @PostMapping
    public ResponseEntity<String> createJobPost(@RequestBody JobPostCreateDto dto) {
        JobPost jobPost = jobPostMapper.toEntity(dto);
        jobPostRepository.save(jobPost);
        return ResponseEntity.ok("职位发布成功");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJobPost(@PathVariable UUID id, @RequestBody JobPostUpdateDto dto) {
        var jobPost = jobPostRepository.findById(id);
        if (jobPost.isEmpty()) {
            return ResponseEntity.badRequest().body("职位不存在");
        }
        
        JobPost existingJobPost = jobPost.get();
        jobPostMapper.partialUpdate(dto, existingJobPost);
        jobPostRepository.save(existingJobPost);
        return ResponseEntity.ok("职位更新成功");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobPost(@PathVariable UUID id) {
        if (jobPostRepository.existsById(id)) {
            jobPostRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        return ResponseEntity.badRequest().body("职位不存在");
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
        return filterService.filterJobPost(filterDto);
    }

    @GetMapping("/by-enterprise/{enterpriseId}")
    public List<JobPost> getJobPostsByEnterprise(@PathVariable UUID enterpriseId) {
        return jobPostRepository.findByEnterpriseId(enterpriseId);
    }

    @GetMapping("/by-job-type")
    public List<JobPost> getJobPostsByType(@RequestParam String jobType) {
        return jobPostRepository.findByJobType(jobType);
    }
}
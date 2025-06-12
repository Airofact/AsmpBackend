package org.airo.asmp.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.JobPostCreateDto;
import org.airo.asmp.dto.entity.JobPostFilterDto;
import org.airo.asmp.dto.entity.JobPostUpdateDto;
import org.airo.asmp.mapper.entity.JobPostMapper;
import org.airo.asmp.model.job.JobPost;
import org.airo.asmp.repository.JobPostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobpost")
@RequiredArgsConstructor
public class JobPostController {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;

    @PostMapping("/add")
    public ResponseEntity<String> createJobPost(@RequestBody JobPostCreateDto dto) {
        JobPost jobPost = jobPostMapper.toEntity(dto);
        jobPostRepository.save(jobPost);
        return ResponseEntity.ok("职位发布成功");
    }

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobPost(@PathVariable UUID id) {
        if (jobPostRepository.existsById(id)) {
            jobPostRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        return ResponseEntity.badRequest().body("职位不存在");
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<JobPost> getJobPost(@PathVariable UUID id) {
        var jobPost = jobPostRepository.findById(id);
        return jobPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public List<JobPost> filterJobPosts(@RequestBody JobPostFilterDto filterDto) {
        return jobPostRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.id() != null) {
                predicates.add(builder.equal(root.get("id"), filterDto.id()));
            }
            if (StringUtils.hasText(filterDto.title())) {
                predicates.add(builder.like(
                    builder.lower(root.get("title")),
                    "%" + filterDto.title().toLowerCase() + "%"
                ));
            }
            if (StringUtils.hasText(filterDto.jobType())) {
                predicates.add(builder.equal(root.get("jobType"), filterDto.jobType()));
            }
            if (filterDto.enterpriseId() != null) {
                predicates.add(builder.equal(root.get("enterprise").get("id"), filterDto.enterpriseId()));
            }
            if (filterDto.salaryMin() > 0) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("salaryMin"), filterDto.salaryMin()));
            }
            if (filterDto.salaryMax() > 0) {
                predicates.add(builder.lessThanOrEqualTo(root.get("salaryMax"), filterDto.salaryMax()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
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
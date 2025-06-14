package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.job.JobApplicationFilterDto;
import org.airo.asmp.model.job.JobApplication;
import org.airo.asmp.repository.JobApplicationRepository;
import org.airo.asmp.util.OptionalSpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobApplicationService {
    
    private final JobApplicationRepository jobApplicationRepository;
    
    /**
     * 根据ID查询工作申请
     */
    public Optional<JobApplication> findById(UUID id) {
        return jobApplicationRepository.findById(id);
    }
    
    /**
     * 查询所有工作申请
     */
    public List<JobApplication> findAll() {
        return jobApplicationRepository.findAll();
    }
      /**
     * 工作申请过滤查询
     */
    public List<JobApplication> findByFilter(JobApplicationFilterDto filterDto) {
        if (filterDto == null) {
            return findAll();
        }
        
        return jobApplicationRepository.findAll((root, query, builder) ->
            OptionalSpecificationBuilder.of(root, builder)
                .equal("id", filterDto.id())
                .equal("jobPost.id", filterDto.jobPostId())
                .equal("alumni.id", filterDto.alumniId())
                .equal("status", filterDto.status())
                .dateTimeAfterOrEqual("applyTime", filterDto.applyTime())
                .build()
        );
    }
}

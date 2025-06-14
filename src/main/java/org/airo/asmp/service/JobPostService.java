package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.job.JobPostFilterDto;
import org.airo.asmp.model.job.JobPost;
import org.airo.asmp.repository.JobPostRepository;
import org.airo.asmp.util.SpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostService {
    
    private final JobPostRepository jobPostRepository;
    private final EnterpriseService enterpriseService;
    
    /**
     * 根据ID查询工作职位
     */
    public Optional<JobPost> findById(UUID id) {
        return jobPostRepository.findById(id);
    }
    
    /**
     * 查询所有工作职位
     */
    public List<JobPost> findAll() {
        return jobPostRepository.findAll();
    }
    
    /**
     * 工作职位过滤查询
     */
    public List<JobPost> findByFilter(JobPostFilterDto filter) {
        if (filter == null) {
            return findAll();
        }
        
        var publishers = filter.enterpriseFilter() != null ? 
            enterpriseService.findByFilter(filter.enterpriseFilter()) : null;
            
        return jobPostRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                    .like("title", filter.title())
                    .like("description", filter.description())
                    .equal("jobType", filter.jobType())
                    .greaterThanOrEqualTo("salaryMax", filter.salaryMaxLowerBound())
                    .lessThanOrEqualTo("salaryMax", filter.salaryMaxUpperBound())
                    .greaterThanOrEqualTo("salaryMin", filter.salaryMinLowerBound())
                    .lessThanOrEqualTo("salaryMin", filter.salaryMinUpperBound())
                    .dateTimeAfterOrEqual("publishTime", filter.publishTimeStart())
                    .dateTimeBefore("publishTime", filter.publishTimeEnd())
                    .in("publisher", publishers)
                    .build()
        );
    }
}

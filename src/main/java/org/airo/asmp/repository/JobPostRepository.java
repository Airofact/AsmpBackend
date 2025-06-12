package org.airo.asmp.repository;

import org.airo.asmp.model.job.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JobPostRepository extends JpaRepository<JobPost, UUID>, JpaSpecificationExecutor<JobPost> {
    
    List<JobPost> findByTitleContaining(String title);
    
    List<JobPost> findByEnterpriseId(UUID enterpriseId);
    
    List<JobPost> findByJobType(String jobType);
    
    List<JobPost> findByDescription(String description);

    @Query("SELECT jp FROM JobPost jp WHERE jp.salaryMin <= :maxSalary AND jp.salaryMax >= :minSalary")
    List<JobPost> findBySalaryRange(@Param("minSalary") int minSalary, @Param("maxSalary") int maxSalary);
}
package org.airo.asmp.repository;

import org.airo.asmp.model.job.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID>, JpaSpecificationExecutor<JobApplication> {
    
    List<JobApplication> findByJobPostId(UUID jobPostId);
    
    List<JobApplication> findByAlumniId(UUID alumniId);
    
    List<JobApplication> findByStatus(JobApplication.ApplicationStatus status);
    
    // 查询特定职位和校友的申请
    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.id = :jobPostId AND ja.alumni.id = :alumniId")
    Optional<JobApplication> findByJobPostIdAndAlumniId(@Param("jobPostId") UUID jobPostId, @Param("alumniId") UUID alumniId);
    
    // 统计某职位的申请数量
    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.jobPost.id = :jobPostId")
    Long countByJobPostId(@Param("jobPostId") UUID jobPostId);
    
    // 根据企业ID查询申请
    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.enterprise.id = :enterpriseId")
    List<JobApplication> findByEnterpriseId(@Param("enterpriseId") UUID enterpriseId);
}
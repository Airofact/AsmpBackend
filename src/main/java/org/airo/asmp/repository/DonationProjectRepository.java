package org.airo.asmp.repository;

import org.airo.asmp.model.donation.DonationProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DonationProjectRepository extends JpaRepository<DonationProject, UUID>, JpaSpecificationExecutor<DonationProject> {
    
    // 根据项目名称查询
    List<DonationProject> findByNameContainingIgnoreCase(String name);
    Page<DonationProject> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // 根据状态查询
    List<DonationProject> findByStatus(DonationProject.ProjectStatus status);
    Page<DonationProject> findByStatus(DonationProject.ProjectStatus status, Pageable pageable);
    
    // 根据分类查询
    List<DonationProject> findByCategory(String category);
    Page<DonationProject> findByCategory(String category, Pageable pageable);
    
    // 根据发起者查询
    List<DonationProject> findByOrganizerId(UUID organizerId);
    Page<DonationProject> findByOrganizerId(UUID organizerId, Pageable pageable);
    
    // 根据目标金额范围查询
    List<DonationProject> findByTargetAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    Page<DonationProject> findByTargetAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // 根据当前金额范围查询
    List<DonationProject> findByCurrentAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    Page<DonationProject> findByCurrentAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // 根据开始时间范围查询
    List<DonationProject> findByStartDateBetween(LocalDateTime startTime, LocalDateTime endTime);
    Page<DonationProject> findByStartDateBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // 根据结束时间范围查询
    List<DonationProject> findByEndDateBetween(LocalDateTime startTime, LocalDateTime endTime);
    Page<DonationProject> findByEndDateBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    // 查询活跃项目（状态为ACTIVE且未过结束时间）
    List<DonationProject> findByStatusAndEndDateAfter(DonationProject.ProjectStatus status, LocalDateTime currentTime);
    Page<DonationProject> findByStatusAndEndDateAfter(DonationProject.ProjectStatus status, LocalDateTime currentTime, Pageable pageable);
    
    // 查询已达到目标金额的项目
    List<DonationProject> findByCurrentAmountGreaterThanEqual(BigDecimal targetAmount);
    
    // 根据发起者和状态查询
    List<DonationProject> findByOrganizerIdAndStatus(UUID organizerId, DonationProject.ProjectStatus status);
    
    // 根据分类和状态查询
    List<DonationProject> findByCategoryAndStatus(String category, DonationProject.ProjectStatus status);
    Page<DonationProject> findByCategoryAndStatus(String category, DonationProject.ProjectStatus status, Pageable pageable);
    
    // 查询某个时间段内创建的项目
    List<DonationProject> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // 根据名称精确查询
    DonationProject findByName(String name);
}

package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.donation.DonationProjectCreateDto;
import org.airo.asmp.dto.donation.DonationProjectUpdateDto;
import org.airo.asmp.dto.donation.DonationProjectFilterDto;
import org.airo.asmp.mapper.DonationProjectMapper;
import org.airo.asmp.model.entity.BusinessEntity;
import org.airo.asmp.model.donation.DonationProject;
import org.airo.asmp.repository.entity.BusinessEntityRepository;
import org.airo.asmp.repository.DonationProjectRepository;
import org.airo.asmp.util.OptionalSpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DonationProjectService {
    
    private final DonationProjectRepository donationProjectRepository;
    private final BusinessEntityRepository<BusinessEntity> businessEntityRepository;
    private final DonationProjectMapper donationProjectMapper;
    private final BusinessEntityService businessEntityService;
    
    /**
     * 创建捐赠项目
     */
    @Transactional
    public DonationProject createProject(DonationProjectCreateDto createDto) {
        // 验证发起者是否存在
        BusinessEntity organizer = null;
        if (createDto.organizerId() != null) {
            organizer = businessEntityRepository.findById(createDto.organizerId())
                    .orElseThrow(() -> new RuntimeException("项目发起者不存在"));
        }
        
        // 验证项目名称是否已存在
        if (donationProjectRepository.findByName(createDto.name()) != null) {
            throw new RuntimeException("项目名称已存在");
        }
        
        // 创建项目实体
        DonationProject project = donationProjectMapper.createDtoToEntity(createDto);
        project.setOrganizer(organizer);
        project.setStatus(DonationProject.ProjectStatus.ACTIVE);
        project.setCurrentAmount(BigDecimal.ZERO);
        
        return donationProjectRepository.save(project);
    }
    
    /**
     * 更新捐赠项目
     */
    @Transactional
    public DonationProject updateProject(UUID id, DonationProjectUpdateDto updateDto) {
        DonationProject project = donationProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        // 如果要更新发起者
        if (updateDto.organizerId() != null) {
            BusinessEntity organizer = businessEntityRepository.findById(updateDto.organizerId())
                    .orElseThrow(() -> new RuntimeException("项目发起者不存在"));
            project.setOrganizer(organizer);
        }
        
        // 如果要更新项目名称，检查是否重复
        if (updateDto.name() != null && !updateDto.name().equals(project.getName())) {
            DonationProject existingProject = donationProjectRepository.findByName(updateDto.name());
            if (existingProject != null && !existingProject.getId().equals(id)) {
                throw new RuntimeException("项目名称已存在");
            }
        }
        
        donationProjectMapper.updateEntityFromDto(updateDto, project);
        
        return donationProjectRepository.save(project);
    }
    
    /**
     * 删除捐赠项目
     */
    @Transactional
    public void deleteProject(UUID id) {
        DonationProject project = donationProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        // 检查项目是否可以删除（例如是否有相关捐赠记录）
        if (project.getCurrentAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("已有捐赠的项目不能删除，只能关闭");
        }
        
        donationProjectRepository.delete(project);
    }
    
    /**
     * 根据ID查询项目
     */
    public Optional<DonationProject> findById(UUID id) {
        return donationProjectRepository.findById(id);
    }
      /**
     * 查询所有项目
     */
    public List<DonationProject> findAll() {
        return donationProjectRepository.findAll();
    }
      /**
     * 根据状态查询项目
     */
    public List<DonationProject> findByStatus(DonationProject.ProjectStatus status) {
        return donationProjectRepository.findByStatus(status);
    }
      /**
     * 根据分类查询项目
     */
    public List<DonationProject> findByCategory(String category) {
        return donationProjectRepository.findByCategory(category);
    }
      /**
     * 根据发起者查询项目
     */
    public List<DonationProject> findByOrganizer(UUID organizerId) {
        return donationProjectRepository.findByOrganizerId(organizerId);
    }
      /**
     * 查询活跃项目
     */
    public List<DonationProject> findActiveProjects() {
        return donationProjectRepository.findByStatusAndEndDateAfter(
                DonationProject.ProjectStatus.ACTIVE, LocalDateTime.now());
    }
    
    /**
     * 查询已达到目标的项目
     */
    public List<DonationProject> findTargetReachedProjects() {
        return donationProjectRepository.findAll().stream()
                .filter(DonationProject::isTargetReached)
                .toList();
    }
    
    /**
     * 关闭项目
     */
    @Transactional
    public DonationProject closeProject(UUID id) {
        DonationProject project = donationProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        project.setStatus(DonationProject.ProjectStatus.CLOSED);
        return donationProjectRepository.save(project);
    }
    
    /**
     * 暂停项目
     */
    @Transactional
    public DonationProject suspendProject(UUID id) {
        DonationProject project = donationProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        project.setStatus(DonationProject.ProjectStatus.SUSPENDED);
        return donationProjectRepository.save(project);
    }
    
    /**
     * 完成项目
     */
    @Transactional
    public DonationProject completeProject(UUID id) {
        DonationProject project = donationProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        project.setStatus(DonationProject.ProjectStatus.COMPLETED);
        return donationProjectRepository.save(project);
    }
    
    /**
     * 更新项目当前金额
     */
    @Transactional
    public DonationProject updateCurrentAmount(UUID id, BigDecimal amount) {
        DonationProject project = donationProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        project.setCurrentAmount(amount);
        return donationProjectRepository.save(project);
    }
    
    /**
     * 检查项目是否可以接受捐赠
     */
    public boolean canAcceptDonation(UUID id) {
        DonationProject project = donationProjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        // 检查项目状态
        if (project.getStatus() != DonationProject.ProjectStatus.ACTIVE) {
            return false;
        }
        
        // 检查结束时间
		return project.getEndDate() == null || !project.getEndDate().isBefore(LocalDateTime.now());
	}
	
	/**
     * 根据过滤条件查询捐赠项目
     */
    public List<DonationProject> findByFilter(DonationProjectFilterDto filter) {
        if (filter == null) {
            return findAll();
        }
        
        var organizers = filter.organizerFilter() != null ? 
            businessEntityService.findByFilter(filter.organizerFilter()) : null;
            
        return donationProjectRepository.findAll((root, query, builder) ->
                OptionalSpecificationBuilder.of(root, builder)
                        .in("organizer", organizers)
                        .like("name", filter.name())
                        .like("description", filter.description())
                        .greaterThanOrEqualTo("targetAmount", filter.minTargetAmount())
                        .lessThanOrEqualTo("targetAmount", filter.maxTargetAmount())
                        .greaterThanOrEqualTo("currentAmount", filter.minCurrentAmount())
                        .lessThanOrEqualTo("currentAmount", filter.maxCurrentAmount())
                        .dateTimeAfterOrEqual("startDate", filter.startDateFrom())
                        .dateTimeBefore("startDate", filter.startDateTo())
                        .dateTimeAfterOrEqual("endDate", filter.endDateFrom())
                        .dateTimeBefore("endDate", filter.endDateTo())
                        .equal("status", filter.status())
                        .like("category", filter.category())
                        .equal("targetReached", filter.targetReached())
                        .build()
        );
    }
}

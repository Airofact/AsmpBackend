package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.donation.DonationCreateDto;
import org.airo.asmp.dto.donation.DonationUpdateDto;
import org.airo.asmp.mapper.DonationMapper;
import org.airo.asmp.model.entity.BusinessEntity;
import org.airo.asmp.model.donation.Donation;
import org.airo.asmp.model.donation.DonationProject;
import org.airo.asmp.repository.entity.BusinessEntityRepository;
import org.airo.asmp.repository.DonationProjectRepository;
import org.airo.asmp.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.airo.asmp.dto.donation.DonationFilterDto;
import org.airo.asmp.util.SpecificationBuilder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DonationService {
    
    private final DonationRepository donationRepository;
    private final DonationProjectRepository donationProjectRepository;
    private final BusinessEntityRepository<BusinessEntity> businessEntityRepository;
    private final DonationMapper donationMapper;
    private final AlumniService alumniService;
    private final DonationProjectService donationProjectService;
    
    /**
     * 创建捐赠
     */
    @Transactional
    public Donation createDonation(DonationCreateDto createDto) {
        // 验证捐赠者是否存在
        BusinessEntity donor = businessEntityRepository.findById(createDto.donorId())
                .orElseThrow(() -> new RuntimeException("捐赠者不存在"));
        
        // 验证项目是否存在
        DonationProject project = donationProjectRepository.findById(createDto.projectId())
                .orElseThrow(() -> new RuntimeException("捐赠项目不存在"));
        
        // 检查项目状态是否允许捐赠
        if (project.getStatus() != DonationProject.ProjectStatus.ACTIVE) {
            throw new RuntimeException("项目当前状态不允许捐赠");
        }
        
        // 创建捐赠实体
        Donation donation = donationMapper.createDtoToEntity(createDto);
        donation.setDonor(donor);
        donation.setProject(project);
        donation.setDonateTime(LocalDateTime.now());
        donation.setStatus(Donation.DonationStatus.PENDING);
        
        // 保存捐赠
        donation = donationRepository.save(donation);
        
        // 如果捐赠状态为已确认，更新项目当前金额
        if (donation.getStatus() == Donation.DonationStatus.CONFIRMED || 
            donation.getStatus() == Donation.DonationStatus.COMPLETED) {
            updateProjectAmount(project, donation.getAmount());
        }
        
        return donation;
    }
    
    /**
     * 更新捐赠
     */
    @Transactional
    public Donation updateDonation(UUID id, DonationUpdateDto updateDto) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠记录不存在"));
        
        Donation.DonationStatus oldStatus = donation.getStatus();
        BigDecimal oldAmount = donation.getAmount();
        
        donationMapper.updateEntityFromDto(updateDto, donation);
        
        // 如果状态或金额发生变化，需要更新项目金额
        if (updateDto.status() != null && updateDto.status() != oldStatus ||
            updateDto.amount() != null && !updateDto.amount().equals(oldAmount)) {
            updateProjectAmountOnStatusChange(donation, oldStatus, oldAmount);
        }
        
        return donationRepository.save(donation);
    }
    
    /**
     * 删除捐赠
     */
    @Transactional
    public void deleteDonation(UUID id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠记录不存在"));
        
        // 如果捐赠已确认，需要从项目金额中减去
        if (donation.getStatus() == Donation.DonationStatus.CONFIRMED || 
            donation.getStatus() == Donation.DonationStatus.COMPLETED) {
            DonationProject project = donation.getProject();
            project.setCurrentAmount(project.getCurrentAmount().subtract(donation.getAmount()));
            donationProjectRepository.save(project);
        }
        
        donationRepository.delete(donation);
    }
    
    /**
     * 根据ID查询捐赠
     */
    public Optional<Donation> findById(UUID id) {
        return donationRepository.findById(id);
    }
      /**
     * 查询所有捐赠
     */
    public List<Donation> findAll() {
        return donationRepository.findAll();
    }

    /**
     * 根据捐赠者查询
     */
    public List<Donation> findByDonor(UUID donorId) {
        return donationRepository.findByDonorId(donorId);
    }
      /**
     * 根据项目查询
     */
    public List<Donation> findByProject(UUID projectId) {
        return donationRepository.findByProjectId(projectId);
    }
    
    /**
     * 计算项目总捐赠金额
     */
    public BigDecimal calculateProjectTotalAmount(UUID projectId) {
        List<Donation> donations = donationRepository.findByProjectIdAndStatus(
                projectId, Donation.DonationStatus.CONFIRMED);
        donations.addAll(donationRepository.findByProjectIdAndStatus(
                projectId, Donation.DonationStatus.COMPLETED));
        
        return donations.stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 确认捐赠
     */
    @Transactional
    public Donation confirmDonation(UUID id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠记录不存在"));
        
        if (donation.getStatus() != Donation.DonationStatus.PENDING) {
            throw new RuntimeException("只能确认待确认状态的捐赠");
        }
        
        donation.setStatus(Donation.DonationStatus.CONFIRMED);
        donation = donationRepository.save(donation);
        
        // 更新项目金额
        updateProjectAmount(donation.getProject(), donation.getAmount());
        
        return donation;
    }
    
    /**
     * 完成捐赠
     */
    @Transactional
    public Donation completeDonation(UUID id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("捐赠记录不存在"));
        
        if (donation.getStatus() != Donation.DonationStatus.CONFIRMED) {
            throw new RuntimeException("只能完成已确认状态的捐赠");
        }
        
        donation.setStatus(Donation.DonationStatus.COMPLETED);
        return donationRepository.save(donation);
    }
    
    /**
     * 更新项目金额
     */
    private void updateProjectAmount(DonationProject project, BigDecimal amount) {
        project.setCurrentAmount(project.getCurrentAmount().add(amount));
        donationProjectRepository.save(project);
    }
    
    /**
     * 状态变化时更新项目金额
     */
    private void updateProjectAmountOnStatusChange(Donation donation, Donation.DonationStatus oldStatus, BigDecimal oldAmount) {
        DonationProject project = donation.getProject();
        
        // 如果旧状态已计入金额，先减去
        if (oldStatus == Donation.DonationStatus.CONFIRMED || oldStatus == Donation.DonationStatus.COMPLETED) {
            project.setCurrentAmount(project.getCurrentAmount().subtract(oldAmount));
        }
        
        // 如果新状态需要计入金额，再加上
        if (donation.getStatus() == Donation.DonationStatus.CONFIRMED || 
            donation.getStatus() == Donation.DonationStatus.COMPLETED) {
            project.setCurrentAmount(project.getCurrentAmount().add(donation.getAmount()));
        }
        
        donationProjectRepository.save(project);
    }
    
    /**
     * 根据过滤条件查询捐赠
     */
    public List<Donation> findByFilter(DonationFilterDto filter) {
        if (filter == null) {
            return findAll();
        }
        
        var donors = filter.donorFilter() != null ? 
            alumniService.findByFilter(filter.donorFilter()) : null;
        var donationProjects = filter.donationProjectFilter() != null ? 
            donationProjectService.findByFilter(filter.donationProjectFilter()) : null;
            
        return donationRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                    .in("donor", donors)
                    .in("project", donationProjects)
                    .greaterThanOrEqualTo("amount", filter.minAmount())
                    .lessThanOrEqualTo("amount", filter.maxAmount())
                    .equal("paymentMethod", filter.paymentMethod())
                    .dateTimeAfterOrEqual("donateTime", filter.donateTimeFrom())
                    .dateTimeBefore("donateTime", filter.donateTimeTo())
                    .equal("status", filter.status())
                    .equal("anonymous", filter.anonymous())
                    .like("transactionId", filter.transactionId())
                    .dateTimeAfterOrEqual("createdAt", filter.createdAtFrom())
                    .dateTimeBefore("createdAt", filter.createdAtTo())
                    .build()
        );
    }
}

package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.donation.DonationFilterDto;
import org.airo.asmp.dto.donation.DonationProjectCreateDto;
import org.airo.asmp.dto.donation.DonationProjectFilterDto;
import org.airo.asmp.dto.donation.DonationProjectUpdateDto;
import org.airo.asmp.model.donation.Donation;
import org.airo.asmp.model.donation.DonationProject;
import org.airo.asmp.service.DonationProjectService;
import org.airo.asmp.service.DonationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/donation-project")
@RequiredArgsConstructor
public class DonationProjectController {

    private final DonationProjectService donationProjectService;
    private final DonationService donationService;

    /**
     * 创建捐赠项目
     */
    @PostMapping
    public ResponseEntity<DonationProject> createProject(@Valid @RequestBody DonationProjectCreateDto createDto) {

            DonationProject project = donationProjectService.createProject(createDto);
            return new ResponseEntity<>(project, HttpStatus.CREATED);

    }

    /**
     * 更新捐赠项目
     */
    @PutMapping("/{id}")
    public ResponseEntity<DonationProject> updateProject(@PathVariable UUID id,
                                                        @Valid @RequestBody DonationProjectUpdateDto updateDto) {

            DonationProject project = donationProjectService.updateProject(id, updateDto);
            return ResponseEntity.ok(project);
    }
      /**
     * 删除捐赠项目
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {

            donationProjectService.deleteProject(id);
            return ResponseEntity.noContent().build();

    }

    /**
     * 根据ID查询项目
     */
    @GetMapping("/{id}")
    public ResponseEntity<DonationProject> getProjectById(@PathVariable UUID id) {
        return donationProjectService.findById(id)
                .map(project -> ResponseEntity.ok(project))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
      /**
     * 查询所有项目
     */
    @GetMapping
    public ResponseEntity<List<DonationProject>> getAllProjects() {
        List<DonationProject> projects = donationProjectService.findAll();
        return ResponseEntity.ok(projects);
    }

    /**
     * 根据条件查询项目
     */    @PostMapping("/filter")
    public ResponseEntity<List<DonationProject>> searchProjects(@RequestBody DonationProjectFilterDto filter) {
        List<DonationProject> projects = donationProjectService.findByFilter(filter);
        return ResponseEntity.ok(projects);
    }

    /**
     * 根据状态查询项目
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DonationProject>> getProjectsByStatus(@PathVariable DonationProject.ProjectStatus status) {
        List<DonationProject> projects = donationProjectService.findByStatus(status);
        return ResponseEntity.ok(projects);
    }

    /**
     * 根据分类查询项目
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<DonationProject>> getProjectsByCategory(@PathVariable String category) {
        List<DonationProject> projects = donationProjectService.findByCategory(category);
        return ResponseEntity.ok(projects);
    }

    /**
     * 根据发起者查询项目
     */
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<DonationProject>> getProjectsByOrganizer(@PathVariable UUID organizerId) {
        List<DonationProject> projects = donationProjectService.findByOrganizer(organizerId);
        return ResponseEntity.ok(projects);
    }

    /**
     * 查询活跃项目
     */
    @GetMapping("/active")
    public ResponseEntity<List<DonationProject>> getActiveProjects() {
        List<DonationProject> projects = donationProjectService.findActiveProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * 查询已达到目标的项目
     */
    @GetMapping("/target-reached")
    public ResponseEntity<List<DonationProject>> getTargetReachedProjects() {
        List<DonationProject> projects = donationProjectService.findTargetReachedProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * 关闭项目
     */
    @PostMapping("/{id}/close")
    public ResponseEntity<DonationProject> closeProject(@PathVariable UUID id) {

            DonationProject project = donationProjectService.closeProject(id);
            return ResponseEntity.ok(project);

    }

    /**
     * 暂停项目
     */
    @PostMapping("/{id}/suspend")
    public ResponseEntity<DonationProject> suspendProject(@PathVariable UUID id) {
            DonationProject project = donationProjectService.suspendProject(id);
            return ResponseEntity.ok(project);

    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<DonationProject> resumeProject(@PathVariable UUID id) {
            DonationProject project = donationProjectService.resumeProject(id);
            return ResponseEntity.ok(project);
    }

    /**
     * 完成项目
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<DonationProject> completeProject(@PathVariable UUID id) {
            DonationProject project = donationProjectService.completeProject(id);
            return ResponseEntity.ok(project);

    }

    /**
     * 检查项目是否可以接受捐赠
     */
    @GetMapping("/{id}/can-donate")
    public ResponseEntity<Boolean> canAcceptDonation(@PathVariable UUID id) {

            boolean canDonate = donationProjectService.canAcceptDonation(id);
            return ResponseEntity.ok(canDonate);

    }

    /**
     * 更新项目当前金额
     */
    @PutMapping("/{id}/amount")
    public ResponseEntity<DonationProject> updateCurrentAmount(@PathVariable UUID id,
                                                              @RequestParam BigDecimal amount) {
            DonationProject project = donationProjectService.updateCurrentAmount(id, amount);
            return ResponseEntity.ok(project);

    }

    /**
     * 获取项目进度信息
     */
    @GetMapping("/{id}/progress")
    public ResponseEntity<ProjectProgressInfo> getProjectProgress(@PathVariable UUID id) {
        return donationProjectService.findById(id)
                .map(project -> {
                    ProjectProgressInfo info = new ProjectProgressInfo(
                            project.getCurrentAmount(),
                            project.getTargetAmount(),
                            project.getProgress(),
                            project.isTargetReached()
                    );
                    return ResponseEntity.ok(info);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
      /**
     * 项目进度信息DTO
     */
    public record ProjectProgressInfo(
            BigDecimal currentAmount,
            BigDecimal targetAmount,
            Double progressPercentage,
            Boolean targetReached
    ) {}

    // ===== 全局捐赠相关端点 =====

    /**
     * 获取所有捐赠（不限特定项目）
     */
    @GetMapping("/donation")
    public ResponseEntity<List<Donation>> getAllDonations() {
        List<Donation> donations = donationService.findAll();
        return ResponseEntity.ok(donations);
    }

    /**
     * 捐赠过滤查询（不限特定项目）
     */
    @PostMapping("/donation/filter")
    public ResponseEntity<List<Donation>> filterDonations(
            @RequestBody DonationFilterDto filterDto) {
        List<Donation> donations = donationService.findByFilter(filterDto);
        return ResponseEntity.ok(donations);
    }
}

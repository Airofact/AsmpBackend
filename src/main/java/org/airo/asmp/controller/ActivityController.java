package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.activity.ActivityApplicationFilterDto;
import org.airo.asmp.dto.activity.ActivityCreateDto;
import org.airo.asmp.dto.activity.ActivityFilterDto;
import org.airo.asmp.dto.activity.ActivityStatusCountDto;
import org.airo.asmp.dto.activity.ActivityTimeRangeDto;
import org.airo.asmp.dto.activity.ActivityUpdateDto;
import org.airo.asmp.mapper.activity.ActivityMapper;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.activity.ActivityApplication;
import org.airo.asmp.model.activity.Status;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.repository.ActivityRepository;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.airo.asmp.service.ActivityApplicationService;
import org.airo.asmp.service.ActivityService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity")
public class ActivityController {
    private final ActivityRepository activityRepository;
    private final OrganizationRepository organizationRepository;
    private final ActivityMapper activityMapper;
    private final ActivityService activityService;
    private final ActivityApplicationService activityApplicationService;    // 活动注册
    @PostMapping
    public ResponseEntity<Activity> add(@Valid @RequestBody ActivityCreateDto activityCreateDto) {
        Organization organization = organizationRepository.findById(activityCreateDto.organizer()).orElse(null);
        if (organization == null) {
            return ResponseEntity.badRequest().build();
        }
        if(activityCreateDto.startTime().isBefore(activityCreateDto.endTime())) {
            Activity activity = activityMapper.toEntity(activityCreateDto);
            activity.setOrganizer(organization);
            Activity savedActivity = activityRepository.save(activity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedActivity);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 活动信息修改
    @PutMapping("/{id}")
    public ResponseEntity<Activity> update(@PathVariable UUID id, @Valid @RequestBody ActivityUpdateDto activityUpdateDto) {
        var activity = activityRepository.findById(id);
        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        if(activityUpdateDto.startTime().isAfter(activityUpdateDto.endTime())) {
            return ResponseEntity.badRequest().build();
        }

        Activity existingActivity = activity.get();
        activityMapper.partialUpdate(activityUpdateDto, existingActivity);
        Activity updatedActivity = activityRepository.save(existingActivity);
        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        if (optionalActivity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Activity activity = optionalActivity.get();
        if (LocalDateTime.now().isAfter(activity.getStartTime())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            activityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // 根据ID查询活动
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable UUID id) {
        Optional<Activity> activity = activityRepository.findById(id);
        return activity.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getAll() {
        return ResponseEntity.ok(activityRepository.findAll());
    }

    // 活动分组查询（包含状态过滤）
    @PostMapping("/filter")
    public List<Activity> filter(@RequestBody ActivityFilterDto activityFilterDto) {
        return activityService.findByFilter(activityFilterDto);
    }

    @GetMapping("/statusCount")
    public List<ActivityStatusCountDto> getActivityCountByStatus() {
        return activityService.getActivityCountByStatus();
    }

    // 1. 查询活动列表（时间段）
    @PostMapping("/query/timeRange")
    public List<Activity> queryByTimeRange(@RequestBody ActivityTimeRangeDto dto) {
        return activityService.getActivitiesInTimeRange(dto.start(), dto.end());
    }

    // 2. 时间段内按状态分组统计
    @PostMapping("/stat/timeRange")
    public List<ActivityStatusCountDto> statByTimeRange(@RequestBody ActivityTimeRangeDto dto) {
        return activityService.getActivityCountByStatusInTimeRange(dto.start(), dto.end());
    }

    // 根据organizerid查询
    @GetMapping("/byOrganizer/{organizerId}")
    public List<Activity> getActivitiesByOrganizer(@PathVariable("organizerId") UUID organizerId) {
        return activityRepository.findByOrganizerId(organizerId);
    }

    // 根据title查询
    @GetMapping("/byTitle")
    public List<Activity> getActivitiesByTitle(@RequestParam("title") String title) {
        return activityRepository.findByTitleContaining(title);
    }
      // 根据status查询活动
    @GetMapping("/byStatus")
    public List<Activity> getActivitiesByStatus(@RequestParam("status") String status) {
        try {
            Status statusEnum = Status.valueOf(status);
            return activityService.getActivitiesByStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("无效的状态值: " + status);
        }
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Alumni>> getParticipants(@PathVariable UUID id) {
        return ResponseEntity.ok(activityApplicationService.getParticipants(id));
    }

    @GetMapping("/{id}/participate-rate")
    public ResponseEntity<Double> getParticipateRate(@PathVariable UUID id) {
        return ResponseEntity.ok(activityApplicationService.getParticipateRate(id));
    }
    
    // ===== 全局活动申请相关端点 =====
    
    /**
     * 获取所有活动申请（不限特定活动）
     */
    @GetMapping("/application")
    public ResponseEntity<List<ActivityApplication>> getAllApplications() {
        List<ActivityApplication> applications = activityApplicationService.findAll();
        return ResponseEntity.ok(applications);
    }
    
    /**
     * 活动申请过滤查询（不限特定活动）
     */
    @PostMapping("/application/filter")
    public ResponseEntity<List<ActivityApplication>> filterApplications(
            @RequestBody ActivityApplicationFilterDto filterDto) {
        List<ActivityApplication> applications = activityApplicationService.findByFilter(filterDto);
        return ResponseEntity.ok(applications);
    }
}


package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.activity.ActivityCreateDto;
import org.airo.asmp.dto.activity.ActivityFilterDto;
import org.airo.asmp.dto.activity.ActivityStatusCountDto;
import org.airo.asmp.dto.activity.ActivityTimeRangeDto;
import org.airo.asmp.dto.activity.ActivityUpdateDto;
import org.airo.asmp.mapper.activity.ActivityMapper;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.activity.Status;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.repository.ActivityRepository;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.airo.asmp.service.ActivityService;
import org.airo.asmp.service.FilterService;
import org.springdoc.core.annotations.ParameterObject;
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
    private final FilterService filterService;

    // 活动注册
    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody ActivityCreateDto activityCreateDto) {
        Organization organization = organizationRepository.findById(activityCreateDto.organizer()).orElseThrow(() -> new RuntimeException("组织不存在"));
        if(activityCreateDto.startTime().isBefore(activityCreateDto.endTime())) {
            Activity activity = activityMapper.toEntity(activityCreateDto);
            activity.setOrganizer(organization);
            activityRepository.save(activity);
            return ResponseEntity.ok("活动添加成功！");
        } else {
            return ResponseEntity.badRequest().body("添加失败，请检查日期");
        }
    }

    // 活动信息修改
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @Valid @RequestBody ActivityUpdateDto activityUpdateDto) {
        var activity = activityRepository.findById(id);
        if (activity.isEmpty()) {
            return ResponseEntity.badRequest().body("id为 %s 的活动不存在！".formatted(id));
        }
        
        if(activityUpdateDto.startTime().isAfter(activityUpdateDto.endTime())) {
            return ResponseEntity.badRequest().body("修改失败，请检查日期");
        }

        Activity existingActivity = activity.get();
        activityMapper.partialUpdate(activityUpdateDto, existingActivity);
        activityRepository.save(existingActivity);
        return ResponseEntity.ok("活动信息修改成功！");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        Optional<Activity> optionalActivity = activityRepository.findById(id);
        if (optionalActivity.isEmpty()) {
            return ResponseEntity.badRequest().body("id为 %s 的活动不存在！".formatted(id));
        }
        Activity activity = optionalActivity.get();
        if (LocalDateTime.now().isBefore(activity.getStartTime())) {
            activityRepository.deleteById(id);
            return ResponseEntity.ok("活动删除成功！");
        } else {
            return ResponseEntity.badRequest().body("活动已经在举行或已结束，无法删除");
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
    @GetMapping("/filter")
    public List<Activity> filter(@RequestBody ActivityFilterDto activityFilterDto) {
        return filterService.filterActivity(activityFilterDto);
    }

    @GetMapping("/statusCount")
    public List<ActivityStatusCountDto> getActivityCountByStatus() {
        return activityService.getActivityCountByStatus();
    }    // 1. 查询活动列表（时间段）
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
    }    // 根据title查询
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
}


package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.ActivityCreateDto;
import org.airo.asmp.dto.entity.ActivityStatusCountDto;
import org.airo.asmp.dto.entity.ActivityTimeRangeDto;
import org.airo.asmp.dto.entity.ActivityUpdateDto;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.entity.Organization;
import org.airo.asmp.repository.ActivityRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.repository.entity.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    //增加活动
    @PostMapping("/add")
    public ResponseEntity<String> addActivity(@RequestBody ActivityCreateDto dto) {
        Organization organization = organizationRepository.findById(dto.organizer()).orElseThrow(() -> new RuntimeException("Organization not found"));
        if(dto.startTime().isBefore(dto.endTime())) {
            Activity activity = new Activity();
            activity.setStartTime(dto.startTime());
            activity.setEndTime(dto.endTime());
            activity.setLocation(dto.location());
            activity.setDescription(dto.description());
            activity.setTitle(dto.title());
            activity.setStatus(dto.status());
            activity.setMaxParticipants(dto.maxParticipants());
            activity.setOrganizer(organization);
            activityRepository.save(activity);
            return ResponseEntity.ok("活动添加成功");
        }
        else {
            return ResponseEntity.ok("添加失败，请检查日期");
        }
    }

    // 删除活动
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteActivity(@PathVariable("id") UUID id) {

        if (activityRepository.existsById(id)) {
            Optional<Activity> optionalactivity = activityRepository.findById(id);
            Activity activity= optionalactivity.get();
            if (LocalDateTime.now().isBefore(activity.getStartTime())) {
                activityRepository.delete(activity);
                return ResponseEntity.ok("活动删除成功");
            } else {
                return ResponseEntity.ok("活动已经在举行或已结束，无法删除");
            }
        }
        else {
            return ResponseEntity.ok("未找到活动");
        }
    }

    //修改活动
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateActivity(@PathVariable("id") UUID id, @RequestBody ActivityUpdateDto newData) {
        if (activityRepository.existsById(id)&&newData.startTime().isBefore(newData.endTime())) {
            Optional<Activity> optionalactivity = activityRepository.findById(id);
            Activity updateactivity= optionalactivity.get();
            updateactivity.setStartTime(newData.startTime());
            updateactivity.setTitle(newData.title());
            updateactivity.setDescription(newData.description());
            updateactivity.setEndTime(newData.endTime());
            updateactivity.setLocation(newData.location());
            updateactivity.setMaxParticipants(newData.maxParticipants());
            updateactivity.setStatus(newData.status());
            updateactivity.setOrganizer(updateactivity.getOrganizer());
            activityRepository.save(updateactivity);
            return ResponseEntity.ok("修改成功");
        }
        else if (newData.startTime().isAfter(newData.endTime()) ){
            return ResponseEntity.ok("修改失败，请检查日期");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
//查询活动
    @GetMapping("/search/{id}")
    public ResponseEntity<Activity> searchActivity(@PathVariable("id") UUID id) {
        if (activityRepository.existsById(id)) {
            Optional<Activity> optionalactivity = activityRepository.findById(id);
            return ResponseEntity.ok(optionalactivity.get());
        }
        else {
            return ResponseEntity.ok(null);
        }
    }
    @GetMapping("/statusCount")
    public List<ActivityStatusCountDto> getActivityCountByStatus() {
        return activityRepository.countByStatusGroup();
    }

    // 1. 查询活动列表（时间段）
    @PostMapping("/query/timeRange")
    public List<Activity> queryByTimeRange(@RequestBody ActivityTimeRangeDto dto) {
        return activityRepository.findByTimeRange(dto.start(), dto.end());
    }

    // 2. 时间段内按状态分组统计
    @PostMapping("/stat/timeRange")
    public List<ActivityStatusCountDto> statByTimeRange(@RequestBody ActivityTimeRangeDto dto) {
        return activityRepository.countByStatusInTimeRange(dto.start(), dto.end());
    }
    //根据organizerid查询
    @GetMapping("/byOrganizer/{organizerId}")
    public List<Activity> getActivitiesByOrganizer(@PathVariable("organizerId") UUID organizerId) {
        return activityRepository.findByOrganizerId(organizerId);
    }
    //根据title查询
    @GetMapping("/byTitle")
    public List<Activity> getActivitiesByTitle(@RequestParam("title") String title) {
        return activityRepository.findByTitle(title);
    }
}


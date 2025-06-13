package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.activity.ActivityStatusCountDto;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.activity.Status;
import org.airo.asmp.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    
    private final ActivityRepository activityRepository;
    
    /**
     * 获取所有活动按状态分组的统计
     */
    public List<ActivityStatusCountDto> getActivityCountByStatus() {
        List<Activity> allActivities = activityRepository.findAll();
        
        Map<Status, Long> statusCounts = allActivities.stream()
                .collect(Collectors.groupingBy(
                        Activity::getStatus, // 使用计算属性
                        Collectors.counting()
                ));
        
        return statusCounts.entrySet().stream()
                .map(entry -> new ActivityStatusCountDto(entry.getKey(), entry.getValue()))
                .toList();
    }
    
    /**
     * 获取指定时间段内的活动按状态分组的统计
     */
    public List<ActivityStatusCountDto> getActivityCountByStatusInTimeRange(LocalDateTime start, LocalDateTime end) {
        List<Activity> activitiesInRange = activityRepository.findByStartTimeBetween(start, end);
        
        Map<Status, Long> statusCounts = activitiesInRange.stream()
                .collect(Collectors.groupingBy(
                        Activity::getStatus, // 使用计算属性
                        Collectors.counting()
                ));
        
        return statusCounts.entrySet().stream()
                .map(entry -> new ActivityStatusCountDto(entry.getKey(), entry.getValue()))
                .toList();
    }
    
    /**
     * 获取指定时间段内的活动列表
     */
    public List<Activity> getActivitiesInTimeRange(LocalDateTime start, LocalDateTime end) {
        return activityRepository.findByStartTimeBetween(start, end);
    }
    
    /**
     * 根据状态过滤活动（在服务层计算）
     */
    public List<Activity> getActivitiesByStatus(Status status) {
        return activityRepository.findAll().stream()
                .filter(activity -> activity.getStatus() == status)
                .toList();
    }
}

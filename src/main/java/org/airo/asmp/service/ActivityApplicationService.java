package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.activity.ActivityApplicationFilterDto;
import org.airo.asmp.model.activity.ActivityAlumniId;
import org.airo.asmp.model.activity.ActivityApplication;
import org.airo.asmp.repository.ActivityApplicationRepository;
import org.airo.asmp.util.OptionalSpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityApplicationService {
    
    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityService activityService;
    private final AlumniService alumniService;
    
    /**
     * 根据复合主键查询活动申请
     */
    public Optional<ActivityApplication> findById(ActivityAlumniId id) {
        return activityApplicationRepository.findById(id);
    }
    
    /**
     * 查询所有活动申请
     */
    public List<ActivityApplication> findAll() {
        return activityApplicationRepository.findAll();
    }
    
    /**
     * 活动申请过滤查询
     */
    public List<ActivityApplication> findByFilter(ActivityApplicationFilterDto filterDto) {
        if (filterDto == null) {
            return findAll();
        }
        
        var activities = filterDto.activityFilter() != null ? 
            activityService.findByFilter(filterDto.activityFilter()) : null;
        var alumni = filterDto.alumniFilter() != null ? 
            alumniService.findByFilter(filterDto.alumniFilter()) : null;
            
        return activityApplicationRepository.findAll((root, query, builder) ->
            OptionalSpecificationBuilder.of(root, builder)
                    .in("activity", activities)
                    .in("alumni", alumni)
                    .dateTimeAfterOrEqual("applyTime", filterDto.applyTimeBegin())
                    .dateTimeBefore("applyTime", filterDto.applyTimeEnd())
                    .equal("signedIn", filterDto.signedIn())
                    .build()
        );
    }
}

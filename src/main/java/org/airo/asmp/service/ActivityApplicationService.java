package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.activity.ActivityApplicationFilterDto;
import org.airo.asmp.model.activity.ActivityAlumniId;
import org.airo.asmp.model.activity.ActivityApplication;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.repository.ActivityApplicationRepository;
import org.airo.asmp.util.OptionalSpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<ActivityApplication> findByActivityId(UUID activityId) {
        return activityApplicationRepository.findAll((root, query, builder) ->
                root.get("activity").get("id").in(activityId)
        );
    }

    public List<ActivityApplication> findByAlumniId(UUID alumniId) {
        return activityApplicationRepository.findAll((root, query, builder) ->
                root.get("alumni").get("id").in(alumniId)
        );
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

    public List<ActivityApplication> findSignedIn(UUID activityId) {
        return activityApplicationRepository.findAll((root, query, builder) ->
                builder.and(
                        root.get("activity").get("id").in(activityId),
                        builder.isTrue(root.get("signedIn"))
                )
        );
    }

    public List<Alumni> getParticipants(UUID activityId) {
        return activityApplicationRepository.findAll((root, query, builder) ->
                root.get("activity").get("id").in(activityId)
        ).stream()
          .map(ActivityApplication::getAlumni)
          .toList();
    }

    public Double getParticipateRate (UUID actId) {
        List<Alumni> participants = getParticipants(actId);
        var signedInCount = findSignedIn(actId).size();
        return participants.isEmpty() ? 0.0 : (double) signedInCount / participants.size();
    }
}

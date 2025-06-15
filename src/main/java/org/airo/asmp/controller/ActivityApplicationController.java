package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.activity.ActivityApplicationCreateDto;
import org.airo.asmp.dto.activity.ActivityApplicationFilterDto;
import org.airo.asmp.dto.activity.ActivityApplicationUpdateDto;
import org.airo.asmp.mapper.activity.ActivityApplicationMapper;
import org.airo.asmp.model.activity.ActivityAlumniId;
import org.airo.asmp.model.activity.ActivityApplication;
import org.airo.asmp.repository.ActivityApplicationRepository;
import org.airo.asmp.repository.ActivityRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.service.ActivityApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity/{actId}/application")
@RequiredArgsConstructor
public class ActivityApplicationController {
    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityRepository activityRepository;
    private final AlumniRepository alumniRepository;
    private final ActivityApplicationMapper activityApplicationMapper;
    private final ActivityApplicationService activityApplicationService;

    @PostMapping
    public ResponseEntity<String> add(@PathVariable UUID actId, 
                                     @Valid @RequestBody ActivityApplicationCreateDto activityApplicationCreateDto) {
		UUID alumniId = activityApplicationCreateDto.alumniId();

        var activity = activityRepository.findById(actId);
        if (activity.isEmpty()) {
            return ResponseEntity.badRequest().body("无效活动，请查看是否录入");
        }
        var alumni = alumniRepository.findById(alumniId);
        if (alumni.isEmpty()) {
            return ResponseEntity.badRequest().body("不存在该校友");
        }

        ActivityApplication application = activityApplicationMapper.toEntity(activityApplicationCreateDto);
        // 设置复合主键
        ActivityAlumniId id = new ActivityAlumniId();
        id.setActivityId(actId);
        id.setAlumniId(alumniId);
        application.setId(id);
        application.setActivity(activity.get());
        application.setAlumni(alumni.get());
        
        application.setApplyTime(LocalDateTime.now());
        activityApplicationRepository.save(application);
        return ResponseEntity.status(HttpStatus.CREATED).body("已提交申请");
    }

    @GetMapping("/{alumniId}")
    public ResponseEntity<ActivityApplication> getById(@PathVariable UUID actId, 
                                                      @PathVariable UUID alumniId) {
        ActivityAlumniId id = new ActivityAlumniId();
        id.setActivityId(actId);
        id.setAlumniId(alumniId);
        
        Optional<ActivityApplication> application = activityApplicationRepository.findById(id);
        return application.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ActivityApplication>> getAll(@PathVariable UUID actId) {
        List<ActivityApplication> applications = activityApplicationRepository.findAll()
                .stream()
                .filter(app -> app.getId().getActivityId().equals(actId))
                .toList();
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{alumniId}")
    public ResponseEntity<String> update(@PathVariable UUID actId, 
                                        @PathVariable UUID alumniId,
                                        @Valid @RequestBody ActivityApplicationUpdateDto activityApplicationUpdateDto) {
        ActivityAlumniId id = new ActivityAlumniId();
        id.setActivityId(actId);
        id.setAlumniId(alumniId);
        
        var application = activityApplicationRepository.findById(id);
        if (application.isEmpty()) {
            return ResponseEntity.badRequest().body("id为 %s 的申请不存在！".formatted(id));
        }

        ActivityApplication existingApplication = application.get();
        activityApplicationMapper.partialUpdate(activityApplicationUpdateDto, existingApplication);
        activityApplicationRepository.save(existingApplication);
        return ResponseEntity.ok("申请信息修改成功！");
    }

    @DeleteMapping("/{alumniId}")
    public ResponseEntity<String> delete(@PathVariable UUID actId, @PathVariable UUID alumniId) {
        ActivityAlumniId id = new ActivityAlumniId();
        id.setActivityId(actId);
        id.setAlumniId(alumniId);

        if (!activityApplicationRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("id为 %s 的申请不存在！".formatted(id));
        }

        activityApplicationRepository.deleteById(id);
        return ResponseEntity.ok("申请删除成功！");
    }

    @PostMapping("/{alumniId}/sign-in")
    public ResponseEntity<String> signIn(@PathVariable UUID actId, @PathVariable UUID alumniId) {
        ActivityAlumniId id = new ActivityAlumniId();
        id.setActivityId(actId);
        id.setAlumniId(alumniId);

        var application = activityApplicationRepository.findById(id);
        if (application.isEmpty()) {
            return ResponseEntity.badRequest().body("id为 %s 的申请不存在！".formatted(id));
        }

        ActivityApplication existingApplication = application.get();
        existingApplication.setSignedIn(true);
        activityApplicationRepository.save(existingApplication);
        return ResponseEntity.ok("签到成功！");
    }

    // 申请分组查询
    @PostMapping("/filter")
    public List<ActivityApplication> filter(@PathVariable UUID actId, 
                                           @RequestBody ActivityApplicationFilterDto activityApplicationFilterDto) {
        // 先获取该活动的所有申请，然后应用过滤条件
        return activityApplicationService.findByFilter(activityApplicationFilterDto)
                .stream()
                .filter(app -> app.getId().getActivityId().equals(actId))
                .toList();
    }
}


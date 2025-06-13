package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.activity.ActivityApplicationCreateDto;
import org.airo.asmp.dto.activity.ActivityApplicationFilterDto;
import org.airo.asmp.dto.activity.ActivityApplicationUpdateDto;
import org.airo.asmp.mapper.activity.ActivityApplicationMapper;
import org.airo.asmp.model.activity.ActivityApplication;
import org.airo.asmp.repository.ActivityApplicationRepository;
import org.airo.asmp.repository.ActivityRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.airo.asmp.service.FilterService;
import org.airo.asmp.util.SpecificationBuilder;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity/apply")
@RequiredArgsConstructor
public class ActivityApplicationController {
    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityRepository activityRepository;
    private final AlumniRepository alumniRepository;
    private final ActivityApplicationMapper activityApplicationMapper;
    private final FilterService filterService;

    @PostMapping
    public ResponseEntity<String> add(@Valid @RequestBody ActivityApplicationCreateDto activityApplicationCreateDto) {
        UUID activityId = activityApplicationCreateDto.activityId();
        UUID alumniId = activityApplicationCreateDto.alumniId();

        if (!activityRepository.existsById(activityId)) {
            return ResponseEntity.badRequest().body("无效活动，请查看是否录入");
        }
        if (!alumniRepository.existsById(alumniId)) {
            return ResponseEntity.badRequest().body("不存在该校友");
        }

        ActivityApplication application = activityApplicationMapper.toEntity(activityApplicationCreateDto);
        application.setApplyTime(LocalDateTime.now());
        activityApplicationRepository.save(application);
        return ResponseEntity.status(HttpStatus.CREATED).body("已提交申请");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityApplication> getById(@PathVariable UUID id) {
        Optional<ActivityApplication> application = activityApplicationRepository.findById(id);
        return application.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ActivityApplication>> getAll() {
        List<ActivityApplication> applications = activityApplicationRepository.findAll();
        return ResponseEntity.ok(applications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @Valid @RequestBody ActivityApplicationUpdateDto activityApplicationUpdateDto) {
        var application = activityApplicationRepository.findById(id);
        if (application.isEmpty()) {
            return ResponseEntity.badRequest().body("id为 %s 的申请不存在！".formatted(id));
        }

        ActivityApplication existingApplication = application.get();
        activityApplicationMapper.partialUpdate(activityApplicationUpdateDto, existingApplication);
        activityApplicationRepository.save(existingApplication);
        return ResponseEntity.ok("申请信息修改成功！");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        if (!activityApplicationRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("id为 %s 的申请不存在！".formatted(id));
        }

        activityApplicationRepository.deleteById(id);
        return ResponseEntity.ok("申请删除成功！");
    }

    // 申请分组查询
    @GetMapping("/filter")
    public List<ActivityApplication> filter(@RequestBody ActivityApplicationFilterDto activityApplicationFilterDto) {
        return filterService.filterActivityApplication(activityApplicationFilterDto);
    }
}


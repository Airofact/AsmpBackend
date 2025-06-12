package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.entity.ActivityApplicationCreateDto;
import org.airo.asmp.model.Admin;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.airo.asmp.model.entity.Alumni;
import org.airo.asmp.repository.ActivityApplicationRepository;
import org.airo.asmp.repository.ActivityRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity_application")

public class ActivityApplicationController {
    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityRepository activityRepository;
    private final AlumniRepository alumniRepository;

    //增加申请
    @PostMapping("/add")
    public ResponseEntity<String> addApplication(@RequestBody ActivityApplicationCreateDto dto) {
        Alumni alumni = alumniRepository.findById(dto.alumni()).orElseThrow(() -> new RuntimeException("不存在该学生"));
        Activity activity = activityRepository.findById(dto.activity()).orElseThrow(() -> new RuntimeException("不存在该活动"));
            ActivityApplication activityApplication = new ActivityApplication();
            activityApplication.setAlumni(alumni);
            activityApplication.setActivity(activity);
            activityApplication.setApplyTime(LocalDateTime.now());
            activityApplicationRepository.save(activityApplication);
            return ResponseEntity.ok("已提交申请");
    }
    //删除申请
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteApplication(@PathVariable("id") UUID id)
        {
        if (activityApplicationRepository.existsById(id)) {
            activityApplicationRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        else {
            return ResponseEntity.ok("数据不存在");
        }
        }
        /*暂且不知道需不需要
        //修改申请
        @PutMapping("/update/{id}")
    public ResponseEntity<String> updateActivity(@PathVariable("id") UUID id, @RequestBody ActivityApplication newData) {
        if (activityApplicationRepository.existsById(id)) {
            Optional<ActivityApplication> optionalActivityApplication = activityApplicationRepository.findById(id);
            ActivityApplication application = optionalActivityApplication.get();
            application.setApplyTime();
            activityApplicationRepository.save(application);
            return ResponseEntity.ok("修改成功");
        }
        else {
            return ResponseEntity.ok("数据不存在");
        }
        }*/
        //查询申请
        @GetMapping("/search/{id}")
    public ResponseEntity<ActivityApplication> searchActivity(@PathVariable("id") UUID id) {
        if (activityApplicationRepository.existsById(id)) {
            Optional<ActivityApplication> optionalActivityApplication = activityApplicationRepository.findById(id);
            return ResponseEntity.ok(optionalActivityApplication.get());
        }
        else {
            return ResponseEntity.ok(null);
        }
        }
//根据realname，title，signedin进行申请的查询
    @GetMapping("/search")
    public ResponseEntity<List<ActivityApplication>> searchApplications(
            @RequestParam(name = "realName",required = false) String realName,
            @RequestParam(name = "title",required = false) String title,
            @RequestParam(name = "signedIn",required = false) Boolean signedIn) {
        List<ActivityApplication> result = activityApplicationRepository
                .findByRealNameAndTitleAndSignedIn(realName, title, signedIn);
        return ResponseEntity.ok(result);
    }
    }


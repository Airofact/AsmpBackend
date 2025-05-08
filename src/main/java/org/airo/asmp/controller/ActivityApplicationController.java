package org.airo.asmp.controller;

import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.airo.asmp.repository.ActivityApplicationRepository;
import org.airo.asmp.repository.ActivityRepository;
import org.airo.asmp.repository.entity.AlumniRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity_application")

public class ActivityApplicationController {
    private final ActivityApplicationRepository activityApplicationRepository;
    private final ActivityRepository activityRepository;
    private final AlumniRepository alumniRepository;
    // 构造函数注入
    public ActivityApplicationController(ActivityApplicationRepository activityApplicationRepository,
                                         ActivityRepository activityRepository, AlumniRepository alumniRepository) {
        this.activityApplicationRepository = activityApplicationRepository;
        this.activityRepository = activityRepository;
        this.alumniRepository = alumniRepository;
    }


    //增加申请
    @PostMapping("/add")
    public ResponseEntity<String> addApplication(@RequestBody ActivityApplication application) {
        String activityId = application.getActivity().getId();
        UUID alumniId = application.getAlumni().getId();
        if (!activityRepository.existsById(activityId)) {return ResponseEntity.ok("不存在该活动");}
        else if (!alumniRepository.existsById(alumniId)){return ResponseEntity.ok("不存在该学生");}
        else {
            activityApplicationRepository.save(application);
            return ResponseEntity.ok("已提交申请");}

    }
    //删除申请
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteApplication(@PathVariable("id") String id)
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
    public ResponseEntity<String> updateActivity(@PathVariable("id") String id, @RequestBody ActivityApplication newData) {
        if (activityApplicationRespository.existsById(id)) {
            Optional<ActivityApplication> optionalActivityApplication = activityApplicationRespository.findById(id);
            ActivityApplication application = optionalActivityApplication.get();
            application.setApplyTime();
            activityApplicationRespository.save(application);
            return ResponseEntity.ok("修改成功");
        }
        else {
            return ResponseEntity.ok("数据不存在");
        }
        }*/
        //查询申请
        @GetMapping("/search/{id}")
    public ResponseEntity<ActivityApplication> searchActivity(@PathVariable("id") String id) {
        if (activityApplicationRepository.existsById(id)) {
            Optional<ActivityApplication> optionalActivityApplication = activityApplicationRepository.findById(id);
            return ResponseEntity.ok(optionalActivityApplication.get());
        }
        else {
            return ResponseEntity.ok(null);
        }
        }
    }


package org.airo.asmp.controller;

import org.airo.asmp.model.Activity_application.Activity_application;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.repository.activity_application.Activity_applicationRespository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/activity_application")

public class Activity_applicationController {
    private final Activity_applicationRespository activity_applicationRepository;

    // 构造函数注入
    public Activity_applicationController(Activity_applicationRespository Activity_applicationRespository) {
        this.activity_applicationRepository = Activity_applicationRespository;
    }


    //增加申请
    @PostMapping("/add")
    public ResponseEntity<String> addApplication(@RequestBody Activity_application application) {
        activity_applicationRepository.save(application);
        return ResponseEntity.ok("已提交申请");
    }
    //删除申请
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteApplication(@PathVariable("id") Long id)
        {
        if (activity_applicationRepository.existsById(id)) {
            activity_applicationRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        else {
            return ResponseEntity.ok("数据不存在");
        }
        }
        //修改申请
        @PutMapping("/update/{id}")
    public ResponseEntity<String> updateActivity(@PathVariable("id") Long id, @RequestBody Activity_application newData) {
        if (activity_applicationRepository.existsById(id)) {
            Optional<Activity_application> optionalActivity_application = activity_applicationRepository.findById(id);
            Activity_application application = optionalActivity_application.get();
            application.setTitle(newData.getTitle());
            application.setAlumni_id(newData.getAlumni_id());
            application.setDescription(newData.getDescription());
            activity_applicationRepository.save(application);
            return ResponseEntity.ok("修改成功");
        }
        else {
            return ResponseEntity.ok("未数据不存在");
        }
        }
        //查询申请
        @GetMapping("/search/{id}")
    public ResponseEntity<Activity_application> searchActivity(@PathVariable("id") Long id) {
        if (activity_applicationRepository.existsById(id)) {
            Optional<Activity_application> optionalActivity_application = activity_applicationRepository.findById(id);
            return ResponseEntity.ok(optionalActivity_application.get());
        }
        else {
            return ResponseEntity.ok(null);
        }
        }
    }


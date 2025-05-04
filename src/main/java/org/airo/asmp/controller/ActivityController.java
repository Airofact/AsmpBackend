package org.airo.asmp.controller;

import jakarta.validation.Valid;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.repository.activity.ActivityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    private final ActivityRepository activityRepository;

    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    //增加活动
    @PostMapping("/add")
    public ResponseEntity<String> addActivity(@RequestBody  Activity activity) {
        if(activity.getStart_time().isBefore(activity.getEnd_time())) {
            activityRepository.save(activity);
            return ResponseEntity.ok("活动添加成功");
        }
        else {
            return ResponseEntity.ok("添加失败，请检查日期");
        }
    }

    // 删除活动
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteActivity(@PathVariable("id") Long id) {

        if (activityRepository.existsById(id)) {
            Optional<Activity> optionalactivity = activityRepository.findById(id);
            Activity activity= optionalactivity.get();
            if (LocalDateTime.now().isBefore(activity.getStart_time())) {
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
    public ResponseEntity<String> updateActivity(@PathVariable("id") Long id, @RequestBody Activity newData) {
        if (activityRepository.existsById(id)&&newData.getStart_time().isBefore(newData.getEnd_time())) {
            Optional<Activity> optionalactivity = activityRepository.findById(id);
            Activity updateactivity= optionalactivity.get();

            updateactivity.setStart_time(newData.getStart_time());
            updateactivity.setCreator_id(newData.getCreator_id());
            updateactivity.setTitle(newData.getTitle());
            updateactivity.setDescription(newData.getDescription());
            updateactivity.setEnd_time(newData.getEnd_time());
            updateactivity.setOrg_id(newData.getOrg_id());
            updateactivity.setLocation(newData.getLocation());
            updateactivity.setMax_participants(newData.getMax_participants());
            activityRepository.save(updateactivity);
            return ResponseEntity.ok("修改成功");
        }
        else if (newData.getStart_time().isAfter(newData.getEnd_time()) ){
            return ResponseEntity.ok("修改失败，请检查日期");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
//查询活动
    @GetMapping("/search/{id}")
    public ResponseEntity<Activity> searchActivity(@PathVariable("id") Long id) {
        if (activityRepository.existsById(id)) {
            Optional<Activity> optionalactivity = activityRepository.findById(id);
            return ResponseEntity.ok(optionalactivity.get());
        }
        else {
            return ResponseEntity.ok(null);
        }
    }
}

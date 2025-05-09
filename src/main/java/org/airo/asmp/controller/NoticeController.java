package org.airo.asmp.controller;

import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.airo.asmp.model.notice.Notice;
import org.airo.asmp.repository.NoticeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/notice")
public class NoticeController {
    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    //增加通知
    @PostMapping("/add")
    public ResponseEntity<String> addActivity(@RequestBody Notice notice) {
        noticeRepository.save(notice);
        return ResponseEntity.ok("添加成功");
    }


    //修改通知
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateActivity(@RequestBody Notice newData, @PathVariable("id") String id) {
        if (noticeRepository.existsById(id)) {
            Optional<Notice> optionalNotice = noticeRepository.findById(id);
            Notice updateNotice=optionalNotice.get();
            updateNotice.setTitle(newData.getTitle());
            updateNotice.setContent(newData.getContent());
            updateNotice.setType(newData.getType());
            noticeRepository.save(updateNotice);
            return ResponseEntity.ok("修改成功");
        }
        else{
            return ResponseEntity.ok("未找到通知");
        }
    }

    //查询活动
    @GetMapping("search/{id}")
    public ResponseEntity<Notice> searchActivity(@PathVariable("id") String id) {
        if (noticeRepository.existsById(id)) {
            Optional<Notice> optionalNotice = noticeRepository.findById(id);
            return ResponseEntity.ok(optionalNotice.get());
        }
        else{
            return ResponseEntity.ok(null);
        }
    }

    //删除活动
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteActivity(@PathVariable("id") String id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        else{
            return ResponseEntity.ok("未找到通知");
        }
    }
}

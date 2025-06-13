package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.model.activity.Activity;
import org.airo.asmp.model.notice.Notice;
import org.airo.asmp.model.notice.Type;
import org.airo.asmp.repository.NoticeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.airo.asmp.dto.entity.NoticeCreateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notice")
public class NoticeController {
    private final NoticeRepository noticeRepository;
    //增加通知
    @PostMapping("/add")
    public ResponseEntity<String> addActivity(@RequestBody NoticeCreateDto dto) {
        Notice notice = new Notice();
        notice.setTitle(dto.title());
        notice.setContent(dto.content());
        notice.setType(dto.type());
        noticeRepository.save(notice);
        return ResponseEntity.ok("添加成功");
    }


    //修改通知
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateActivity(@RequestBody NoticeCreateDto newData, @PathVariable("id") UUID id) {
        if (noticeRepository.existsById(id)) {
            Optional<Notice> optionalNotice = noticeRepository.findById(id);
            Notice updateNotice=optionalNotice.get();
            updateNotice.setTitle(newData.title());
            updateNotice.setContent(newData.content());
            updateNotice.setType(newData.type());
            noticeRepository.save(updateNotice);
            return ResponseEntity.ok("修改成功");
        }
        else{
            return ResponseEntity.ok("未找到通知");
        }
    }

    //查询
    @GetMapping("search/{id}")
    public ResponseEntity<Notice> searchActivity(@PathVariable("id") UUID id) {
        if (noticeRepository.existsById(id)) {
            Optional<Notice> optionalNotice = noticeRepository.findById(id);
            return ResponseEntity.ok(optionalNotice.get());
        }
        else{
            return ResponseEntity.ok(null);
        }
    }
//根据类型查询
    @GetMapping("/byType")
    public ResponseEntity<List<Notice>> getNoticesByType(@RequestParam ("type") Type type) {
        List<Notice> notices = noticeRepository.findByType(type);
        return ResponseEntity.ok(notices);
    }
//根据title查询
    @GetMapping("/byTitle")
    public ResponseEntity<List<Notice>> getNoticesByTitle(@RequestParam("title") String title) {
        List<Notice> notices = noticeRepository.findByTitle(title);
        return ResponseEntity.ok(notices);
    }
    //查询所有活动
    @GetMapping("/getAll")
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> list = noticeRepository.findAll();
        return ResponseEntity.ok(list);
    }
    //删除
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteActivity(@PathVariable("id") UUID id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return ResponseEntity.ok("删除成功");
        }
        else{
            return ResponseEntity.ok("未找到通知");
        }
    }
}

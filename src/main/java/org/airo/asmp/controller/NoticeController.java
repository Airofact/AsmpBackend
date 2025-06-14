package org.airo.asmp.controller;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.NoticeCreateDto;
import org.airo.asmp.dto.NoticeFilterDto;
import org.airo.asmp.dto.NoticeUpdateDto;
import org.airo.asmp.mapper.NoticeMapper;
import org.airo.asmp.model.notice.Notice;
import org.airo.asmp.service.NoticeService;
import org.airo.asmp.repository.NoticeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final NoticeService noticeService;    // 通知注册
    @PostMapping
    public ResponseEntity<Notice> add(@Valid @RequestBody NoticeCreateDto noticeCreateDto) {
        Notice notice = noticeMapper.toEntity(noticeCreateDto);
        Notice savedNotice = noticeRepository.save(notice);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice);
    }    // 通知信息修改
    @PutMapping("/{id}")
    public ResponseEntity<Notice> update(@PathVariable UUID id, @Valid @RequestBody NoticeUpdateDto noticeUpdateDto) {
        var notice = noticeRepository.findById(id);
        if (notice.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Notice existingNotice = notice.get();
        noticeMapper.partialUpdate(noticeUpdateDto, existingNotice);
        Notice updatedNotice = noticeRepository.save(existingNotice);
        return ResponseEntity.ok(updatedNotice);
    }    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!noticeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            noticeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // 根据ID查询通知
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getById(@PathVariable UUID id) {
        Optional<Notice> notice = noticeRepository.findById(id);
        return notice.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAll() {
        return ResponseEntity.ok(noticeRepository.findAll());
    }    // 通知分组查询
    @PostMapping("/filter")
    public List<Notice> filter(@RequestBody NoticeFilterDto noticeFilterDto) {
        return noticeService.findByFilter(noticeFilterDto);
    }
}

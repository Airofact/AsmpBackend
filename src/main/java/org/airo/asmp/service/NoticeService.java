package org.airo.asmp.service;

import lombok.RequiredArgsConstructor;
import org.airo.asmp.dto.NoticeFilterDto;
import org.airo.asmp.model.notice.Notice;
import org.airo.asmp.repository.NoticeRepository;
import org.airo.asmp.util.SpecificationBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    
    private final NoticeRepository noticeRepository;
    
    /**
     * 根据ID查询通知
     */
    public Optional<Notice> findById(UUID id) {
        return noticeRepository.findById(id);
    }
    
    /**
     * 查询所有通知
     */
    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }
    
    /**
     * 通知过滤查询
     */
    public List<Notice> findByFilter(NoticeFilterDto filterDto) {
        if (filterDto == null) {
            return findAll();
        }
        
        return noticeRepository.findAll((root, query, builder) ->
            SpecificationBuilder.of(root, builder)
                .like("title", filterDto.title())
                .like("content", filterDto.content())
                .equal("type", filterDto.type())
                .build()
        );
    }
}

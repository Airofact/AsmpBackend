package org.airo.asmp.dto;

import org.airo.asmp.model.notice.Notice;

/**
 * DTO for filtering {@link org.airo.asmp.model.notice.Notice}
 */
public record NoticeFilterDto(
        String title,
        String content,
        Notice.Type type
) {}

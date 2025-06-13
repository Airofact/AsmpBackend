package org.airo.asmp.dto;

import org.airo.asmp.model.notice.Notice;

/**
 * DTO for updating {@link org.airo.asmp.model.notice.Notice}
 */
public record NoticeUpdateDto(
        String title,
        String content,
        Notice.Type type
) {}

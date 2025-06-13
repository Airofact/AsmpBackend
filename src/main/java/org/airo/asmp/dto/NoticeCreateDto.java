package org.airo.asmp.dto;

import org.airo.asmp.model.notice.Notice;

public record NoticeCreateDto(String title, String content, Notice.Type type) {}

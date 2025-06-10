package org.airo.asmp.dto.entity;

import org.airo.asmp.model.notice.Type;

public record NoticeCreateDto(String title, String content, Type type) {}

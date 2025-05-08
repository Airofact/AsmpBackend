package org.airo.asmp.repository;

import org.airo.asmp.model.activityapplication.ActivityApplication;
import org.airo.asmp.model.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, String> {
}

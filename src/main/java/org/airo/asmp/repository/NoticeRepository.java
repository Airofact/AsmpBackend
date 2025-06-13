package org.airo.asmp.repository;

import org.airo.asmp.model.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID>, JpaSpecificationExecutor<Notice> {
    List<Notice> findByType(Notice.Type type);

    List<Notice> findByTitle(String title);
}

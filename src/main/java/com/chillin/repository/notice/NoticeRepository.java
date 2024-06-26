package com.chillin.repository.notice;

import com.chillin.domain.News;
import com.chillin.domain.NoticeBoard;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeBoard, Long> {

    Page<NoticeBoard> findByTitleContaining(String title, Pageable pageable);
}

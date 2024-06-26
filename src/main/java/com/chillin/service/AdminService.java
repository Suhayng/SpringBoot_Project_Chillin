package com.chillin.service;

import com.chillin.dto.NewsDTO;
import com.chillin.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<NoticeDTO> noticeList(String searchTxt, Pageable pageable);

    NoticeDTO getNotice(Long noticeId);
}

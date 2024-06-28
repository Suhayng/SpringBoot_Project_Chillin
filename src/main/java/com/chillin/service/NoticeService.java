package com.chillin.service;

import com.chillin.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {
    Page<NoticeDTO> noticeList(String searchTxt, Pageable pageable);

    NoticeDTO getNotice(Long noticeId);

    boolean insertNotice(NoticeDTO dto);

    void delete(Long noticeId);

    Boolean modifyNotice(NoticeDTO dto);

    List<NoticeDTO> mainNoticeList();
}

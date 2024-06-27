package com.chillin.service;

import com.chillin.dto.NewsDTO;
import com.chillin.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.chillin.dto.BoardDTO;
import com.chillin.dto.UserDTO;

import java.util.List;

public interface AdminService {
    Page<NoticeDTO> noticeList(String searchTxt, Pageable pageable);

    NoticeDTO getNotice(Long noticeId);

    List<UserDTO> getUserList();

    List<BoardDTO> getUserBoardList(Long uid);

}

package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.NoticeBoard;
import com.chillin.dto.NewsDTO;
import com.chillin.dto.NoticeDTO;
import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.notice.NoticeRepository;
import com.chillin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

}

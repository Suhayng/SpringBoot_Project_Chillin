package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.NoticeBoard;
import com.chillin.dto.NewsDTO;
import com.chillin.dto.NoticeDTO;
import com.chillin.repository.board.BoardRepository;
import com.chillin.repository.notice.NoticeRepository;
import com.chillin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<NoticeDTO> noticeList(String searchTxt, Pageable pageable) {

        Page<NoticeBoard> noticePage = noticeRepository.findByTitleContaining(searchTxt, pageable);

        return noticePage.map(item -> NoticeDTO.builder()
                .noticeId(item.getNoticeId())
                .title(item.getTitle())
                .content(item.getContent())
                .writeDate(item.getWriteDate())
                .modifyDate(item.getModifyDate())
                .build());

    }

    @Override
    public NoticeDTO getNotice(Long noticeId) {
        Optional<NoticeBoard> noticeBoard = noticeRepository.findById(noticeId);
        NoticeBoard find = noticeBoard.orElseThrow(() -> new RuntimeException());

        return NoticeDTO.builder()
                .noticeId(find.getNoticeId())
                .title(find.getTitle())
                .content(find.getContent())
                .writeDate(find.getWriteDate())
                .modifyDate(find.getModifyDate())
                .build();
    }

    /** 회원 리스트 */
    @Override
    public List<UserDTO> getUserList() {
        List<User> users = userRepository.findAll();

        Page<NoticeBoard> noticePage = noticeRepository.findByTitleContaining(searchTxt, pageable);

        return noticePage.map(item -> NoticeDTO.builder()
                .noticeId(item.getNoticeId())
                .title(item.getTitle())
                .content(item.getContent())
                .writeDate(item.getWriteDate())
                .modifyDate(item.getModifyDate())
                .build());

    }
}

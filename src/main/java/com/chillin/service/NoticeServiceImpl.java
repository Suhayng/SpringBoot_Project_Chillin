package com.chillin.service;

import com.chillin.domain.NoticeBoard;
import com.chillin.dto.NoticeDTO;
import com.chillin.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;

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
}

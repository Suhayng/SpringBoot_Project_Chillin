package com.chillin.service;

import com.chillin.domain.NoticeBoard;
import com.chillin.domain.QNoticeBoard;
import com.chillin.dto.NoticeDTO;
import com.chillin.repository.notice.NoticeRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.chillin.domain.QNoticeBoard.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final JPAQueryFactory jpaQueryFactory;

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

    @Override
    public boolean insertNotice(NoticeDTO dto) {

        boolean result = false;

        NoticeBoard noticeBoard = NoticeBoard.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        NoticeBoard save = noticeRepository.save(noticeBoard);

        Long noticeId = save.getNoticeId();

        if (noticeId > 0) {
            dto.setNoticeId(noticeId);
            dto.setWriteDate(save.getWriteDate());
        }

        result = true;

        return result;
    }

    @Override
    public void delete(Long noticeId) {

        noticeRepository.deleteById(noticeId);

    }

    @Override
    @Transactional
    public Boolean modifyNotice(NoticeDTO dto) {

        NoticeBoard noticeBoard = noticeRepository.findById(dto.getNoticeId())
                .orElseThrow(RuntimeException::new);


        noticeBoard.setTitle(dto.getTitle());
        noticeBoard.setContent(dto.getContent());

        noticeRepository.save(noticeBoard);

        return true;
    }

    @Override
    public List<NoticeDTO> mainNoticeList() {
        List<Tuple> fetch = jpaQueryFactory.select(noticeBoard.title, noticeBoard.noticeId)
                .from(noticeBoard)
                .orderBy(noticeBoard.noticeId.desc())
                .offset(0)
                .limit(5)
                .fetch();

        List<NoticeDTO> noticeDTOList = fetch.stream().map(item -> {
            NoticeDTO dto = NoticeDTO.builder()
                    .title(item.get(noticeBoard.title))
                    .noticeId(item.get(noticeBoard.noticeId))
                    .build();
            return dto;
        }).collect(Collectors.toList());


        return noticeDTOList;
    }
}

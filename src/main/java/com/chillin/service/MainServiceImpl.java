package com.chillin.service;

import com.chillin.dto.BoardDTO;
import com.chillin.repository.main.MainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final MainRepository mainRepository;


    @Override
    public List<BoardDTO> mainBoardList() {
        List<BoardDTO> list = mainRepository.getMainBoardList();

        for(BoardDTO dto : list){
            String timeAgo = getTimeAgo(dto.getWriteDate());
            dto.setTimeAgo(timeAgo);
        }
        return list;
    }

    @Override
    public List<BoardDTO> getDayList() {
        List<BoardDTO> list = mainRepository.getDayList();

        for(BoardDTO dto : list){
            String timeAgo = getTimeAgo(dto.getWriteDate());
            dto.setTimeAgo(timeAgo);
        }
        return list;
    }

    /** 시간 차이 계산 */
    private String getTimeAgo(LocalDateTime dateTime){
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60){
            return seconds +"초 전";
        }else if(minutes < 60){
            return minutes +"분 전";
        }else if (hours < 24){
            return hours + "시간 전";
        }else {
            return days + "일 전";
        }
    }

}

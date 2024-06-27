package com.chillin.repository.main;

import com.chillin.dto.BoardDTO;

import java.util.List;

public interface MainQueryDSL {
    List<BoardDTO> getMainBoardList();

    List<BoardDTO> getDayList();
}

package com.chillin.service;

import com.chillin.dto.BoardDTO;

import java.util.List;

public interface MainService {
    List<BoardDTO> mainBoardList();

    List<BoardDTO> getDayList();
}

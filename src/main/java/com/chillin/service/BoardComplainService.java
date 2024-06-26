package com.chillin.service;

import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.RepComplainDTO;

public interface BoardComplainService {
    void insertBoardComplain(BoardComplainDTO dto,Long uid);

    void insertRepComplain(RepComplainDTO dto, Long uid);
}

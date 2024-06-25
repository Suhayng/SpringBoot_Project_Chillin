package com.chillin.service;

import com.chillin.dto.BoardComplainDTO;

public interface BoardComplainService {
    void insertComplain(BoardComplainDTO dto,Long uid);
}

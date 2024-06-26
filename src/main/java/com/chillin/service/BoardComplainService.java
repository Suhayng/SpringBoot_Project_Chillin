package com.chillin.service;

import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.ComplainManageDTO;
import com.chillin.dto.RepComplainDTO;

import java.util.List;
import java.util.Map;

public interface BoardComplainService {
    void insertBoardComplain(BoardComplainDTO dto,Long uid);

    void insertRepComplain(RepComplainDTO dto, Long uid);

    List<ComplainManageDTO> getBoardList(int page, int pageSize, String type);

    void blinding(Long bid, String action);

    void completing(Long cid);
}

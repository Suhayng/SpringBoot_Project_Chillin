package com.chillin.repository.complain;

import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.ComplainManageDTO;
import com.chillin.dto.RepComplainDTO;

import java.util.List;
import java.util.Map;

public interface BCQueryDSL {
    boolean prevBoardWrite(Long bid, Long uid);

    boolean write(BoardComplainDTO dto, Long uid, boolean completed);

    boolean write(RepComplainDTO dto, Long uid, boolean completed);

    boolean prevRepWrite(Long rid, Long uid);

    List<ComplainManageDTO> getBoardList(int startRow, int pageSize, String type);

    void blinding(Long bid, String action);

    void completing(Long cid);
}

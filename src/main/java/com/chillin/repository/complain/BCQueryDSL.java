package com.chillin.repository.complain;

import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.RepComplainDTO;

public interface BCQueryDSL {
    boolean prevBoardWrite(Long bid, Long uid);

    boolean write(BoardComplainDTO dto, Long uid, boolean completed);

    boolean write(RepComplainDTO dto, Long uid, boolean completed);

    boolean prevRepWrite(Long rid, Long uid);
}

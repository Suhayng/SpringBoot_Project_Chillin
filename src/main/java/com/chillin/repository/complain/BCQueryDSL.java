package com.chillin.repository.complain;

import com.chillin.dto.BoardComplainDTO;

public interface BCQueryDSL {
    boolean prevWrite(Long bid, Long uid);

    boolean write(BoardComplainDTO dto, Long uid, boolean completed);
}

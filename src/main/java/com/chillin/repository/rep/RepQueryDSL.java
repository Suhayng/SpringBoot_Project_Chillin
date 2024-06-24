package com.chillin.repository.rep;

import com.chillin.domain.Rep;
import com.chillin.dto.RepDTO;

import java.util.List;

public interface RepQueryDSL {
    List<RepDTO> getReps(Long bid);

    List<RepDTO> getReps2(Long bid, Long uid);
}

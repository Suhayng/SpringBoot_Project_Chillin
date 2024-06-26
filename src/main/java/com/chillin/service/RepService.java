package com.chillin.service;

import com.chillin.dto.RepDTO;

import java.util.List;
import java.util.Map;

public interface RepService {
    List<RepDTO> getReps(Long bid);

    Long insertRep(RepDTO dto);

    boolean repDelete(Long rid, Long uid);

    List<RepDTO> getReps2(Long bid, Long uid);

    Map<String, Object> boomup(Long rid, Long uid);

    Map<String, Object> boomdown(Long rid, Long uid);
}

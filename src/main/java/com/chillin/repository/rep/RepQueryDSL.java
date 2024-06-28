package com.chillin.repository.rep;

import com.chillin.domain.Rep;
import com.chillin.dto.RepDTO;

import java.util.List;
import java.util.Map;

public interface RepQueryDSL {
    List<RepDTO> getReps(Long bid);

    List<RepDTO> getReps2(Long bid, Long uid);

    String getStatusMine(Long rid, Long uid);

    void deleteBoom(Long rid, Long uid);

    void updateBoom(Long rid, Long uid, boolean b);

    void insertBoom(Long rid, Long uid, boolean b);

    void getOneRepBoom(Long rid,Map<String, Object> map);
}

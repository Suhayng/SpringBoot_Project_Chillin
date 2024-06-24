package com.chillin.repository.board;

import java.util.Map;

public interface BoardQueryDSL {
    Boolean boardMyBoom(Long uid, Long bid);

    void insertBoom(Boolean updown, Long bid, Long uid);

    void deleteBoom(Long bid, Long uid);

    void changeDown(boolean boom, Long bid, Long uid);

    Map<String, Object> getBoardBoom(Long bid);

    Long isBookmarked(Long uid, Long bid);

    void insertBookmark(Long bid, Long uid);

    void deleteBookmark(Long bid, Long uid);
}

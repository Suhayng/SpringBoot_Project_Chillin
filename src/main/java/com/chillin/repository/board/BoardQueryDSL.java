package com.chillin.repository.board;

import com.chillin.dto.BoardDTO;

import java.util.List;
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

    List<BoardDTO> getRecentList(String search, int startRow, int pageSize);

    List<BoardDTO> getDayList();

    List<BoardDTO> getWeekList();

    List<BoardDTO> getBookmarkList(Long uid);

    List<BoardDTO> getMyBoardList(Long uid);

    Long getTotalBoard(String search);
  
    List<BoardDTO> getUserBoardList(Long uid);


    List<BoardDTO> getUserBoard(String nickname);
}

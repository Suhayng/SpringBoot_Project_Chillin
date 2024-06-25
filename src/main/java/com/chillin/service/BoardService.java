package com.chillin.service;

import com.chillin.dto.BoardDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {
    Map<String, Object> fileUpload(String filePath, MultipartFile image);

    boolean insertBoard(BoardDTO dto);

    BoardDTO getDetail(Long bid);

    void delete(Long bid, String id);
    void delete(Long bid);

    boolean modifyBoard(BoardDTO dto);

    Map<String, Object> getBoom(Long uid, Long bid);

    Map<String, Object> boomupBoard(Long uid, Long bid, String status);

    Map<String, Object> boomdownBoard(Long uid, Long bid, String status);

    String isBookmarked(Long uid, Long bid);

    String bookmaring(Long uid, Long bid, String status);

    List<BoardDTO> getRecentList(String search, int iPage, int pageSize);

    List<BoardDTO> getDayList();

    List<BoardDTO> getWeekList();
}

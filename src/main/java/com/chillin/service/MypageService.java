package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.Message;
import com.chillin.dto.BoardDTO;
import com.chillin.dto.MessageDTO;
import com.chillin.dto.UserDTO;

import java.util.List;

public interface MypageService {
    UserDTO getUser(Long userId);

    long modifyUser(UserDTO dto);

    long deleteUser(Long userId);

    List<MessageDTO> getMessageList(Long userId);

    List<MessageDTO> getMessageDetailList(Long userId, Long messageId);

    Long writeMessage(MessageDTO dto);

    List<BoardDTO> getBookmarkList(Long userId);

    List<BoardDTO> getMyBoardList(Long userId);

    void setIsRead(List<MessageDTO> list, Long userId);
}

package com.chillin.service;

import com.chillin.dto.MessageDTO;
import com.chillin.dto.UserDTO;

import java.util.List;

public interface MypageService {
    UserDTO getUser(Long userId);

    long modifyUser(UserDTO dto);

    long deleteUser(Long userId);

    List<MessageDTO> getMessageList(Long userId);
}

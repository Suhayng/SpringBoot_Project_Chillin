package com.chillin.service;

import com.chillin.dto.UserDTO;

public interface MypageService {
    UserDTO getUser(Long userId);

    long modifyUser(UserDTO dto);

    long deleteUser(Long userId);
}

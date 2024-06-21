package com.chillin.service;

import com.chillin.dto.UserDTO;

public interface UserService {
    Long join(UserDTO dto);

    UserDTO findByEmail(String id);


    Long nickNameCheck(String nickName);
}

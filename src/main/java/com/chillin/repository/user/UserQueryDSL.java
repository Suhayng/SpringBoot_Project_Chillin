package com.chillin.repository.user;

import com.chillin.domain.User;
import com.chillin.dto.UserDTO;

public interface UserQueryDSL {
    User findByEmail(String id);
}

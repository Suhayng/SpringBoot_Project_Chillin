package com.chillin.repository.user;

import com.chillin.domain.User;
import static org.assertj.core.api.Assertions.*;

import com.chillin.dto.UserDTO;
import com.chillin.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;


    @Autowired
    private UserService service;

    @Test
    public void findbyEmail(){
//        User user = repository.findByEmail("test@test.com");
//        assertThat(user.getUserId()).isEqualTo(2L);

        UserDTO dto = service.findByEmail("test@test.com");
        assertThat(dto.getPassword()).isEqualTo("1234");
    }
}
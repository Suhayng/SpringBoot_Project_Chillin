package com.chillin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MypageServiceTest {

    @Autowired
    private MypageService service;

    @Test
    public void getUserTest(){
        assertThat(service.getUser(2L).getName()).isEqualTo("test");

    }



}
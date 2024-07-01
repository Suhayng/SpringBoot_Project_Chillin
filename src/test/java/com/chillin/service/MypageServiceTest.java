package com.chillin.service;

import com.chillin.dto.MessageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MypageServiceTest {
/*

    @Autowired
    private MypageService service;

    @Test
    public void getUserTest(){
        assertThat(service.getUser(2L).getName()).isEqualTo("test");

    }

    @Test
    public void getMessageTest(){
        assertThat(service.getMessageList(6L).size()).isEqualTo(2);
    }

    @Test
    public void getDetailTest(){
        assertThat(service.getMessageDetailList(6L, 7L).get(0).getMeid()).isEqualTo(6);
        assertThat(service.getMessageDetailList(6L, 7L).size()).isEqualTo(2);
    }

    @Test
    public void writeMessagae(){
        MessageDTO dto= MessageDTO.builder()
                .sender(1L)
                .receiver(2L)
                .content("test다!!")
                .build();
        assertThat(service.writeMessage(dto)).isEqualTo(1L);
    }
*/


}
package com.chillin.repository.complain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ComplainTest {

    @Autowired
    private BoardComplainRepository bcRepository;

    @Test
    public void test0(){
        //bcRepository.getBoardList(0,2);
    }

}

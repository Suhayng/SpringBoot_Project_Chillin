package com.chillin.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Test
    public void testStartCrawling(){
        System.out.println("크롤링 시작..");
        newsService.startCrawling();
        System.out.println("크롤링 끝..");

    }
}

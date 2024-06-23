package com.chillin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledCrawlerService {

    private final NewsService newsService;

    // 1시간마다 크롤링 수행된다
    @Scheduled(fixedRate = 3600000)
    public void Crawling() {
//        newsService.startCrawling();
    }

}

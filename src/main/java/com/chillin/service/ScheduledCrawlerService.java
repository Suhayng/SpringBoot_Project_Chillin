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

    // 정각마다 크롤링 수행
    @Scheduled(cron = "0 0 0/1 * * *")
    public void Crawling() {
        newsService.startCrawling();
    }

}

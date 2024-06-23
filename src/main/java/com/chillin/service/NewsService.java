package com.chillin.service;

import com.chillin.dto.NewsDTO;

import java.util.List;

public interface NewsService {

    /** 네이버 뉴스 기사 크롤링 */
    void startCrawling();


    List<NewsDTO> findNews();
}

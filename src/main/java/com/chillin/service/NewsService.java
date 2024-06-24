package com.chillin.service;

import com.chillin.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    /** 네이버 뉴스 기사 크롤링 */
    void startCrawling();

    /** 뉴스 목록 */
    Page<NewsDTO> newsList(String searchTxt, Pageable pageable);

}

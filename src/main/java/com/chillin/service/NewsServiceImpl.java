package com.chillin.service;

import com.chillin.config.QueryDSLConfig;
import com.chillin.domain.News;
import com.chillin.domain.QNews;
import com.chillin.dto.NewsDTO;
import com.chillin.repository.news.NewsRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.chillin.domain.QNews.news;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private WebDriver webDriver;  // 셀레니움 webDriver


    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;

    @Override
    public void startCrawling() {

        // ChromeOption
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking"); // 팝업 무시하기
        options.addArguments("headless"); // 창 없이 프로세스 사용
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=1920,1080");
        webDriver = new ChromeDriver(options);

        try {
            // 네이버 뉴스 사회 접근
            webDriver.get("https://news.naver.com/section/102");

            // 교육 탭 엘리먼트 찾기(css 셀렉터)
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(2));
            WebElement educationTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ct_wrap > div.ct_scroll_wrapper > div.column0 > div > ul > li:nth-child(2) > a")));

            // 교육 탭 클릭
            educationTab.click();
//            log.info("교육 탭 클릭 ");

            // 뉴스 리스트 엘리먼트 찾기
            List<WebElement> newsElements = webDriver.findElements(By.cssSelector(".section_latest_article._CONTENT_LIST._PERSIST_META  ul .sa_text"));

//            log.info("뉴스 리스트 엘리먼트....{}", newsElements);
            List<News> newsList = new ArrayList<>();
            for (WebElement newsElement : newsElements) {
                try {
//                    log.info("....newsElement..{}", newsElement);
                    // 뉴스 링크
                    WebElement linkElement = newsElement.findElement(By.cssSelector("a"));
                    String link = linkElement.getAttribute("href");
                    log.info("......link....{}", link);

                    // 링크 존재하는지 확인
                    if (newsRepository.existsByLink(link)) {
                        log.info("이미 존재하는 뉴스 링크..{}", link);
                        continue;
                    }

                    // 뉴스 제목
                    WebElement titleElement = newsElement.findElement(By.cssSelector("a strong"));
                    String title = titleElement.getText();
                    log.info("title....{}", title);

                    // 언론사명
                    WebElement pressElement = newsElement.findElement(By.cssSelector(".sa_text_info .sa_text_info_left .sa_text_press"));
                    String publisher = pressElement.getText();
                    log.info("publisher....{}", publisher);

                    // News 객체 생성 및 리스트에 추가
                    News news = News.builder()
                            .title(title)
                            .publisher(publisher)
                            .link(link)
                            .writeDate(LocalDateTime.now())
                            .build();

                    newsList.add(news);

                } catch (Exception e) {
                    log.error("뉴스 항목을 처리하는 중 오류 발생: ", e);
                }
            }

            // newslist 저장
            newsRepository.saveAll(newsList);
            log.info("newsList 저장 완료!!!!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
    }

    /**
     * 뉴스 목록
     */
    @Override
    public Page<NewsDTO> newsList(String searchTxt, Pageable pageable) {

        Page<News> newsPage = newsRepository.findByTitleContaining(searchTxt, pageable);

        return newsPage.map(news -> NewsDTO.builder()
                .newsId(news.getNewsId())
                .title(news.getTitle())
                .publisher(news.getPublisher())
                .link(news.getLink())
                .writeDate(news.getWriteDate())
                .build());
    }

    /**
     * 메인 뉴스 목록
     */
    @Override
    public List<NewsDTO> mainNewsList() {

        // news 목록 최신순 5개 가져오기
        List<Tuple> fetch = jpaQueryFactory.select(news.title, news.link)
                .from(news)
                .orderBy(news.newsId.desc())
                .offset(0)
                .limit(5)
                .fetch();

  /*      for (Tuple e : fetch) {
            System.out.println(e.get(news.title));
        }*/

        // NewsDTO 타입으로 바꾸기
        List<NewsDTO> newsDTOS = fetch.stream().map(item -> {
            NewsDTO dto = NewsDTO.builder()
                    .title(item.get(news.title))
                    .link(item.get(news.link))
                    .build();
            return dto;
        }).collect(Collectors.toList());


/*        for (NewsDTO e : newsDTOS) {
            System.out.println(e.getTitle());
        }*/

        return newsDTOS;
    }


}


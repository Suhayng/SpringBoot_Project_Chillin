package com.chillin.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.Duration;


import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
public class NewsServiceImplTest {

        private WebDriver driver;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";   // properties 설정
    public static String WEB_DRIVER_PATH = new File("src/main/resources/static/driver/chromedriver").getAbsolutePath(); // 절대 경로

    @BeforeEach
    public void setUp() {
        // WebDriver 설정 (WebDriverManager 사용)
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        // WebDriver 종료
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFirstNewsTitle() {
        // 네이버 뉴스 메인 페이지로 이동
        driver.get("https://news.naver.com/section/102");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement educationTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ct_wrap > div.ct_scroll_wrapper > div.column0 > div > ul > li:nth-child(2) > a")));

        educationTab.click();

        // 첫 번째 뉴스 제목 엘리먼트 찾기
//        WebElement firstNewsTitle = driver.findElement(By.cssSelector("#_SECTION_HEADLINE_LIST_afvnf li:nth-child(1) .sa_text a > strong"));
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(30));
//        WebElement firstNewsTitle = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#_SECTION_HEADLINE_LIST_afvnf > li:nth-child(1) > div > div > div.sa_text > a > strong")));
        WebElement firstNewsTitle = driver.findElement(By.xpath("//*[@id=\"newsct\"]/div[2]/div/div[1]/div[1]/ul/li[1]/div/div/div[2]/a/strong"));

        // 뉴스 제목이 null이 아닌지 확인
        assertNotNull(firstNewsTitle.getText(), "첫 번째 뉴스 제목이 null.");

        // 뉴스 제목 출력
        System.out.println("첫 번째 뉴스 제목: " + firstNewsTitle.getText());
    }
}

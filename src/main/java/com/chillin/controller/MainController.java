package com.chillin.controller;

import com.chillin.domain.Board;
import com.chillin.dto.BoardDTO;
import com.chillin.dto.NewsDTO;
import com.chillin.dto.NoticeDTO;
import com.chillin.service.BoardService;
import com.chillin.service.MainService;
import com.chillin.service.NewsService;
import com.chillin.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final NewsService newsService;
    private final MainService mainService;
    private final NoticeService noticeService;

    @GetMapping("/")
    public String main(Model model){
        List<NewsDTO> newsList = newsService.mainNewsList();
        List<BoardDTO> boardList = mainService.mainBoardList();
        List<BoardDTO> dayList = mainService.getDayList();
        List<NoticeDTO> noticeList = noticeService.mainNoticeList();


        model.addAttribute("newsList", newsList);
        model.addAttribute("boardList", boardList);
        model.addAttribute("dayFavList", dayList);
        model.addAttribute("noticeList", noticeList);

        return "index";
    }

}

package com.chillin.controller;

import com.chillin.domain.Board;
import com.chillin.dto.NewsDTO;
import com.chillin.service.BoardService;
import com.chillin.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final NewsService newsService;
    private final BoardService boardService;

    @GetMapping("/")
    public String main(Model model){
        List<NewsDTO> newsList = newsService.mainNewsList();



        model.addAttribute("newsList", newsList);

        return "index";
    }

}

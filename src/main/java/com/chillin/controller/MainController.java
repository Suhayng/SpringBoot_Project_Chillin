package com.chillin.controller;

import com.chillin.dto.NewsDTO;
import com.chillin.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final NewsService newsService;


    @GetMapping("/")
    public String main(Model model){
        List<NewsDTO> newsList = newsService.mainNewsList();
        System.out.println(newsList.get(0).getTitle());
        System.out.println(newsList.get(1).getTitle());

        model.addAttribute("newsList", newsList);

        return "index";
    }

}

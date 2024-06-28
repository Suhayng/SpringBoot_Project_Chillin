package com.chillin.controller;

import com.chillin.dto.NewsDTO;
import com.chillin.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/info")
    public String newsList(@RequestParam(required = false, defaultValue = "") String searchTxt
            , @PageableDefault(size = 15, page = 0, sort = "newsId", direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {

        Page<NewsDTO> list = newsService.newsList(searchTxt, pageable);

        int pagesize = 5;
        int startPage
                = ((int)(Math.ceil(pageable.getPageNumber()/pagesize)))
                *pagesize+1;
        int endPage=Math.min(startPage+ pagesize-1
                , list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchtxt", searchTxt);

        return "news/newsBoard";
    }

}

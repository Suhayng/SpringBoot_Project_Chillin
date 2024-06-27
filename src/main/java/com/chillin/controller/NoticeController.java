package com.chillin.controller;

import com.chillin.dto.NoticeDTO;
import com.chillin.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;


    /**공지사항 리스트*/
    @GetMapping("/notice")
    public String noticeList(@RequestParam(required = false, defaultValue = "") String searchTxt
            , @PageableDefault(size = 15, page = 0, sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable
            , Model model){

        Page<NoticeDTO> list = noticeService.noticeList(searchTxt, pageable);

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

        return "notice/notice_list";

    }

    /**공지사항 상세페이지*/
    @GetMapping("/notice/{noticeId}")
    public String detailNotice(@PathVariable("noticeId") Long noticeId, Model model){
        NoticeDTO dto = noticeService.getNotice(noticeId);
        model.addAttribute("board", dto);
        return "notice/notice_detail";
    }
}

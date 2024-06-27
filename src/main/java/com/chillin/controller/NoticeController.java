package com.chillin.controller;

import com.chillin.dto.NoticeDTO;
import com.chillin.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;


    /**
     * 공지사항 리스트
     */
    @GetMapping("/notice")
    public String noticeList(@RequestParam(required = false, defaultValue = "") String searchTxt
            , @PageableDefault(size = 15, page = 0, sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {

        Page<NoticeDTO> list = noticeService.noticeList(searchTxt, pageable);

        int pagesize = 5;
        int startPage
                = ((int) (Math.ceil(pageable.getPageNumber() / pagesize)))
                * pagesize + 1;
        int endPage = Math.min(startPage + pagesize - 1
                , list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchtxt", searchTxt);

        return "notice/notice_list";

    }

    /**
     * 공지사항 상세페이지
     */
    @GetMapping("/notice/{noticeId}")
    public String detailNotice(@PathVariable("noticeId") Long noticeId, Model model) {
        NoticeDTO dto = noticeService.getNotice(noticeId);
        model.addAttribute("board", dto);
        return "notice/notice_detail";
    }

    /**
     * 공지사항 글쓰기로 이동
     */
    @GetMapping("/notice/create")
    public String createNotice() {
        return "notice/notice_create";
    }

    /**
     * 공지사항 글 쓰기
     */
    @PostMapping("/notice/create_notice")
    @ResponseBody
    public NoticeDTO createNotice(@RequestBody NoticeDTO dto
            , HttpSession session) {

//        System.out.println("\n\n\n======================");
//        System.out.println(dto.getTitle());
//        System.out.println(dto.getContent());
//        System.out.println("======================\n\n\n");

        Long uid = (Long) session.getAttribute("uid");

        if (uid != 7) {
            dto.setSuccess(false);
        } else {
            boolean success = noticeService.insertNotice(dto);
            dto.setSuccess(success);
        }

        return dto;

    }

    /**
     * 공지사항 삭제
     */
    @GetMapping("/notice/delete/{noticeId}")
    public String deleteNotice(@PathVariable("noticeId") Long noticeId, HttpSession session) {

        Long uid = (Long) session.getAttribute("uid");

        if (uid != 7) {
            return "redirect:/notice/{noticeId}";
        } else {
            noticeService.delete(noticeId);
        }

        return "redirect:/notice";

    }

    /**
     * 공지사항 글 수정 페이지 이동
     */
    @GetMapping("/notice/modify/{noticeId}")
    public String modifyNotice(@PathVariable("noticeId") Long noticeId
            , Model model
            , HttpSession session
    ) {
        Long uid = (Long) session.getAttribute("uid");

        NoticeDTO dto = noticeService.getNotice(noticeId);

        if (uid != 7) {
            return "redirect:/notice/"+noticeId;
        }

        model.addAttribute("board", dto);

        return "notice/notice_modify";

    }

    /**공지사항 글 수정하기*/
    @PostMapping("/notice/modify/{noticeId}")
    @ResponseBody
    public NoticeDTO modifyNotice(@PathVariable("noticeId") Long noticeId
            , @RequestBody NoticeDTO dto
            , HttpSession session
    ){
        Long uid = (Long) session.getAttribute("uid");

        if(uid != 7){
            dto.setSuccess(false);
        }else{
            dto.setNoticeId(noticeId);
            Boolean success = noticeService.modifyNotice(dto);
            dto.setSuccess(success);
        }

        return dto;
    }



}

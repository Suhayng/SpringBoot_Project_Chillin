package com.chillin.controller;

import com.chillin.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;

    private final AdminService adminService;

    /** 관리자 - 회원 관리 */
    @GetMapping("/admin")
    public String MemberManagement(HttpSession session
                                  , Model model) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // null 값 체크
        if (sessionUid == null) {
            sessionUid = 0L;
        }

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {

            List<UserDTO> userList = adminService.getUserList();

            model.addAttribute("userList", userList);
            return "/admin/memberManagement";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }

    @GetMapping("/admin/user/{uid}")
    public String MemberBoardList(@PathVariable Long uid
                                ,Model model){

        List<BoardDTO> boardDTOS = adminService.getUserBoardList(uid);

        return "/admin/memberBoard";
    }






    /**공지사항 리스트*/
    @GetMapping("/notice")
    public String noticeList(@RequestParam(required = false, defaultValue = "") String searchTxt
            , @PageableDefault(size = 15, page = 0, sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable
            , Model model){

        Page<NoticeDTO> list = adminService.noticeList(searchTxt, pageable);

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
        NoticeDTO dto = adminService.getNotice(noticeId);
        model.addAttribute("board", dto);
        return "notice/notice_detail";
    }


}

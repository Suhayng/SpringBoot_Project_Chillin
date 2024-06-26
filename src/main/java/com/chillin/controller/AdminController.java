package com.chillin.controller;


import com.chillin.dto.BoardDTO;
import com.chillin.dto.UserDTO;

import com.chillin.dto.ComplainManageDTO;
import com.chillin.service.BoardComplainService;
import com.chillin.dto.NewsDTO;
import com.chillin.dto.NoticeDTO;

import com.chillin.service.AdminService;
import com.chillin.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final BoardComplainService complainService;


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
            return "admin/memberManagement";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }


    @GetMapping("/admin/user/{uid}")
    public String MemberBoardList(@PathVariable Long uid
                                ,Model model){

        List<BoardDTO> boardDTOS = adminService.getUserBoardList(uid);
        model.addAttribute("boardList" , boardDTOS);
//        model.addAttribute("nickname", nickname);
        return "admin/memberBoard";
    }


    @GetMapping("/admin/complain_manage/board/{type}")
    public String complainManagement(HttpSession session
            , @PathVariable("type") String type
            , @RequestParam(value = "page", defaultValue = "1") String spage
            , Model model) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // null 값 체크
        if (sessionUid == null) {
            sessionUid = 0L;
        }

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {
            int page = Integer.parseInt(spage);
            int pageSize = 10;
            List<ComplainManageDTO> list = complainService.getBoardList(page, pageSize, type);
            model.addAttribute("list", list);
            return "admin/complain_management";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }


    /**
     * 그 글에 대한 신고를 모아보는 곳
     */
    @GetMapping("/admin/complain/board/{bid}")
    public String boardUnionComplain(@PathVariable("bid") Long bid
            , HttpSession session
            , Model model) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {

            List<ComplainManageDTO> list = complainService.boardUnionComplain(bid);
            model.addAttribute("list", list);
            return "admin/complain_management";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }



    @GetMapping("/board/blind/{action}/{bid}")
    public String blinding(@PathVariable("action") String action
            , @PathVariable("bid") Long bid
            , HttpSession session) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {

            complainService.blinding(bid, action);

            return "redirect:/admin/complain_manage/board/blind";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }

    }


    @GetMapping("/board/complain/complete/{cid}")
    public String completing(@PathVariable("cid") Long cid
            , HttpSession session) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {

            complainService.completing(cid);

            return "redirect:/admin/complain_manage/board/complete";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }


    @GetMapping("/admin/complain_manage/rep/{type}")
    public String complainRepManagement(HttpSession session
            , @PathVariable("type") String type
            , @RequestParam(value = "page", defaultValue = "1") String spage
            , Model model) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // null 값 체크
        if (sessionUid == null) {
            sessionUid = 0L;
        }

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {
            int page = Integer.parseInt(spage);
            int pageSize = 10;
            List<ComplainManageDTO> list = complainService.getRepList(page, pageSize, type);
            model.addAttribute("list", list);
            return "admin/rep_comp_manage";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }

    @GetMapping("/admin/complain/rep/{cid}")
    public String repUnionComplain(@PathVariable("cid") Long cid
            , HttpSession session
            , Model model) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {

            List<ComplainManageDTO> list = complainService.repUnionComplain(cid);
            model.addAttribute("list", list);
            return "admin/rep_comp_manage";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }
    @GetMapping("/rep/blind/{action}/{cid}")
    public String repBlinding(@PathVariable("action") String action
            , @PathVariable("cid") Long cid
            , HttpSession session) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {

            complainService.repBlinding(cid, action);

            return "redirect:/admin/complain_manage/rep/blind";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }

    }

    @GetMapping("/rep/complain/complete/{cid}")
    public String repCompleting(@PathVariable("cid") Long cid
            , HttpSession session) {

        Long sessionUid = (Long) session.getAttribute("uid");

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {

            complainService.repCompleting(cid);

            return "redirect:/admin/complain_manage/rep/complete";
        } else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }
    }


}

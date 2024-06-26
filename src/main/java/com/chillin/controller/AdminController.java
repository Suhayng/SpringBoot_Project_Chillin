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

    @GetMapping("/admin")
    public String MemberManagement(HttpSession session
                                  , Model model){

        Long sessionUid = (Long) session.getAttribute("uid");

        // 관리자 uid와 세션 uid 같은지 체크
        if (sessionUid == 7) {


            return "/admin/memberManagement";
        }else {
            log.info("관리자 페이지 접근 권한이 없습니다.");
            return "redirect:/";
        }

    }


}

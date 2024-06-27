package com.chillin.controller;

import com.chillin.dto.BoardDTO;
import com.chillin.dto.UserDTO;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {
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
        model.addAttribute("boardList" , boardDTOS);
        return "/admin/memberBoard";
    }

}

package com.chillin.controller;

import com.chillin.dto.MessageDTO;
import com.chillin.dto.UserDTO;
import com.chillin.service.BoardService;
import com.chillin.service.MypageService;
import com.chillin.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final MypageService mypageService;

    /**마이페이지-회원정보 수정으로 이동*/
    @GetMapping("/mypage/{userId}")
    public String mypageUser(@PathVariable Long userId, Model model){
        UserDTO dto = mypageService.getUser(userId);

        model.addAttribute("dto", dto);

        return "mypage/mypage_modify";

    }

    /**회원정보 수정 결과*/
    @PostMapping("/user_modi")
    public String userModify(@ModelAttribute("dto") UserDTO dto){

            long userId = mypageService.modifyUser(dto);

            return "redirect:/mypage_modify_alert/"+userId;

    }

    /**회원정보 수정 알림 페이지*/
    @GetMapping("/mypage_modify_alert/{userId}")
    public String userModifyAlert(@PathVariable long userId, Model model){
        model.addAttribute("userId", userId);
        return "/mypage/mypage_modify_alert";

    }

    /**회원 탈퇴하기*/
    @GetMapping("/delete_user/{userId}")
    public @ResponseBody long userDelete(@PathVariable Long userId){
        long id = mypageService.deleteUser(userId);

        return id;
    }


    /**마이페이지-북마크로 이동*/
    @GetMapping("/mypage/bookmark/{userId}")
    public String mypageBookmark(@PathVariable Long userId){
        return "mypage/mypage_bookmark";
    }


    /**마이페이지-쪽지로 이동*/
    @GetMapping("/mypage/message/{userId}")
    public String mypageMessage(@PathVariable Long userId, Model model){

        List<MessageDTO> list = mypageService.getMessageList(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("list", list);


        return "mypage/mypage_message";
    }

    /**마이페이지-쪽지 상세페이지로 이동*/
    @GetMapping("/mypage/messagedetail")
    public String mypageMessageDetail(@RequestParam Long userId
            , @RequestParam Long messageId, Model model
    ) {
        return "mypage/mypage_message_detail";
    }

}

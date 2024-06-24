package com.chillin.controller;

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
    public @ResponseBody long userModify(@Valid @ModelAttribute("dto") UserDTO dto
            , BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return 0;
        }else{

            long id = mypageService.modifyUser(dto);

            return id;
        }

    }
}

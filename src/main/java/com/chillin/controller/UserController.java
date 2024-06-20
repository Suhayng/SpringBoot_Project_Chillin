package com.chillin.controller;

import com.chillin.dto.UserDTO;
import com.chillin.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * 로그인 페이지로 이동
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    /**
     * 회원가입 페이지로 이동
     */
    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("dto", new UserDTO());

        return "user/join";
    }

    /**
     * 회원가입 결과
     */
    @PostMapping("/join")
    public String join_result(@Valid @ModelAttribute("dto") UserDTO dto
            , BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "user/join";
        }

        Long result = service.join(dto);

        return "user/join_alert";
    }
}

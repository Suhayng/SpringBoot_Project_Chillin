package com.chillin.controller;

import com.chillin.dto.UserDTO;
import com.chillin.service.BoardService;
import com.chillin.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService, BoardService boardService) {
        this.userService = userService;
    }

    /**
     * 로그인 페이지로 이동
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    /**로그인 결과*/
    @PostMapping("/login_result")
    public String login_result(@RequestParam String id
            , @RequestParam String password
            , HttpSession session
    ){
        if(id!=null && password != null){
            UserDTO dto = userService.findByEmail(id);

            boolean result=false;

            if(password.equals(dto.getPassword())){
                result=true;
            }

            if(result == true){
                Long uid = dto.getUserId();
                session.setAttribute("uid", uid);
                return "redirect:";
            } else{
                return "redirect:/loginfail";
            }
        } else {
            return "redirect:/loginfail";
        }
    }

    /**로그인 실패시 이동 페이지*/
    @GetMapping("/loginfail")
    public String loginfail(){
        return "user/loginfail";
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

        Long result = userService.join(dto);

        return "user/join_alert";
    }
}

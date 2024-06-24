package com.chillin.controller;

import com.chillin.dto.UserDTO;
import com.chillin.service.BoardService;
import com.chillin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 로그인 페이지로 이동
     */
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    /**
     * 로그인 결과
     */
    @PostMapping("/login_result")
    public String login_result(@RequestParam String id
            , @RequestParam String password
            , HttpSession session
    ) {
        if (id != null && password != null) {
            UserDTO dto = userService.findByEmail(id);

            boolean result = false;

            if (password.equals(dto.getPassword())) {
                result = true;
            }

            if (result == true) {
                Long uid = dto.getUserId();
                session.setAttribute("uid", uid);
                return "redirect:";
            } else {
                return "redirect:/loginfail";
            }
        } else {
            return "redirect:/loginfail";
        }
    }

    /**
     * 로그인 실패시 이동 페이지
     */
    @GetMapping("/loginfail")
    public String loginfail() {
        return "user/loginfail";
    }

    /**로그아웃*/
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("uid") != null){

            session.invalidate();
        }

        return "redirect:";
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
    public String join_result(@Valid UserDTO dto
            , BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return "user/join";
        }

        Long result = userService.join(dto);

        return "user/join_alert";
    }

    /**회원가입-닉네임 중복 확인*/
    @PostMapping("/id_check")
    public @ResponseBody int id_check(@RequestBody HashMap<String,String> hm){
        String id = hm.get("id");
        UserDTO dto = userService.findByEmail(id);

        int result = 0;
        if(dto.getUserId()!=null){
            //기존에 아이디가 있으면 1
            result = 1;
        } else {
            //기존에 아이디가 없으면 0
            result = 0;
        }

        return result;
    }

    /**회원가입-닉네임 중복 확인*/
    @PostMapping("/nick_check")
    public @ResponseBody Long nick_check(@RequestBody HashMap<String,String> hm){
        String nickName = hm.get("nickName");
        //기존에 등록된 닉네임 있는지 확인해서 중복 갯수 리턴
        Long result = userService.nickNameCheck(nickName);

        return result;
    }
}

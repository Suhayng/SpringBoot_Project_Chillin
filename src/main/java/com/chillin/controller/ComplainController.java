package com.chillin.controller;

import com.chillin.domain.Rep;
import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.RepComplainDTO;
import com.chillin.service.BoardComplainService;
import com.chillin.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ComplainController {

    private final BoardComplainService bcService;

    @PostMapping("/board/complain")
    public String boardComplain(@ModelAttribute BoardComplainDTO dto
    , HttpSession session){

        Long uid = (Long) session.getAttribute("uid");
        Long bid = dto.getBid();
        if(uid!=null){
            bcService.insertBoardComplain(dto,uid);

        }

        return "redirect:/community/"+dto.getReturn_board();
    }


    @PostMapping("/rep/complain")
    public String repComplain(@ModelAttribute RepComplainDTO dto
            , HttpSession session){
        Long uid = (Long) session.getAttribute("uid");
        if(uid!=null){
            bcService.insertRepComplain(dto,uid);
        }
        return "redirect:/community/"+dto.getReturn_board();
    }



}

package com.chillin.controller;

import com.chillin.dto.RepDTO;
import com.chillin.service.RepService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RepController {

    private final RepService repService;

    @GetMapping("/getReps/{board_id}")
    public List<RepDTO> getReps(@PathVariable("board_id") Long bid) {

        List<RepDTO> repDTOList = repService.getReps(bid);

        return repDTOList;
    }

    @PostMapping("/insertRep/{board_id}")
    public RepDTO insertRep(@PathVariable("board_id") Long bid
            , @RequestBody RepDTO dto
            , HttpSession session) {


        Long uid = (Long) session.getAttribute("uid");
        dto.setUid(uid);
        dto.setBid(bid);
        Long rid = repService.insertRep(dto);


        return dto;
    }

    @DeleteMapping("/community/delete_rep/{rep_id}")
    public boolean deleteRep(@PathVariable(name = "rep_id") Long rid
            , HttpSession session) {
        boolean delete_success = false;

        Long uid = (Long) session.getAttribute("uid");
        if (uid != null) {
            delete_success = repService.repDelete(rid, uid);
        }
        return delete_success;
    }

}

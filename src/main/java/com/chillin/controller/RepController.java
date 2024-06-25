package com.chillin.controller;

import com.chillin.dto.RepDTO;
import com.chillin.service.RepService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RepController {

    private final RepService repService;

    @GetMapping("/getReps/{board_id}")
    public List<RepDTO> getReps(@PathVariable("board_id") Long bid) {

        List<RepDTO> repDTOList = repService.getReps(bid);

        return repDTOList;
    }

    @GetMapping("/getReps2/{board_id}")
    public List<RepDTO> getReps2(@PathVariable("board_id") Long bid,
                                 HttpSession session) {

        Long uid = (Long) session.getAttribute("uid");
        List<RepDTO> repDTOList = repService.getReps2(bid, uid);

        return repDTOList;
    }

    @PostMapping("/insertRep/{board_id}")
    public RepDTO insertRep(@PathVariable("board_id") Long bid
            , @RequestBody RepDTO dto
            , HttpSession session) {


        Long uid = (Long) session.getAttribute("uid");
        if (uid != null) {
            dto.setUid(uid);
            dto.setBid(bid);
            Long rid = repService.insertRep(dto);
        } else {
            return dto;
        }
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

    @PostMapping("/community/rep_boomup/{rid}")
    @ResponseBody
    public Map<String, Object> repBoomup(@PathVariable Long rid
            , HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");

        Map<String, Object> map = repService.boomup(rid,uid);

        return map;
    }
    @PostMapping("/community/rep_boomdown/{rid}")
    @ResponseBody
    public Map<String, Object> repBoomdown(@PathVariable Long rid
            , HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");

        Map<String, Object> map = repService.boomdown(rid,uid);

        return map;
    }

}
/*
*

    @GetMapping("/community/getRepBoom/{rid}")
    public Map<String, Object> getRepBoom(@PathVariable Long rid
            , HttpSession session) {

        Map<String, Object> map = new HashMap<>();
        /* map 에는 boomup개수, boomdown 개수, 내 상태

        return map;
                }

*/
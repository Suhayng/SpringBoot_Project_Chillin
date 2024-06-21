package com.chillin.controller;

import com.chillin.dto.RepDTO;
import com.chillin.service.RepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RepController {

    private final RepService repService;

    @GetMapping("/getReps/{board_id}")
    public List<RepDTO> getReps(@PathVariable("board_id") Long bid){

        List<RepDTO> repDTOList = repService.getReps(bid);

        System.out.println(repDTOList);

        return repDTOList;
    }
    @PostMapping("/insertRep/{board_id}")
    public RepDTO insertRep(@PathVariable("board_id") Long bid,
                            @RequestBody RepDTO dto){

        String suid = "testid";
        dto.setBid(bid);
        Long rid = repService.insertRep(dto,suid);

        System.out.println("rid ------------- : "+rid);

        return dto;
    }

}

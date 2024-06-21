package com.chillin.controller;

import com.chillin.dto.RepDTO;
import com.chillin.service.RepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}

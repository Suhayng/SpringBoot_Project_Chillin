package com.chillin.controller;

import com.chillin.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Value("${spring.servlet.multipart.location}")
    private String filePath;

    private final BoardService boardService;

    @GetMapping("/editor_test")
    public String editorTest(){
        return "board/editor_test";
    }

    @PostMapping("/editor/image")
    @ResponseBody
    public Map<String, Object> editorImage(@RequestParam("upload") MultipartFile image){
        Map<String, Object> data = new HashMap<String, Object>();

        if(image != null) {

            data = boardService.fileUpload(filePath,image);

            String imgPath = null;
        }
        return data;
    }

}

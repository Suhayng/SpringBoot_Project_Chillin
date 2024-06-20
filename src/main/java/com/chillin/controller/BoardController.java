package com.chillin.controller;

import com.chillin.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BoardController {

    @Value("${spring.servlet.multipart.location}")
    private String filePath;

    private final BoardService boardService;

    @GetMapping("/editor_test")
    public String editorTest() {
        return "board/editor_test";
    }

    @PostMapping("/editor/image")
    @ResponseBody
    public Map<String, Object> editorImage(@RequestParam("upload") MultipartFile image) {

        System.out.println(filePath);
        Map<String, Object> data = new HashMap<String, Object>();
        if (image != null) {
            data = boardService.fileUpload(filePath, image);
        }
        return data;
    }

    @GetMapping("/getImage/{file_name}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(
            @PathVariable(name = "file_name") String fileName){

        InputStream in = null;
        ResponseEntity<byte[]> entity = null;
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+","%20");
        try{
            in = new FileInputStream(filePath+"/"+fileName);
            HttpHeaders headers=new HttpHeaders();
            entity=new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(in)
                    ,headers,  HttpStatus.OK);
        }catch (IOException e){
            System.out.println("getImage 에서 에러"+e);
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return entity;
    }


}

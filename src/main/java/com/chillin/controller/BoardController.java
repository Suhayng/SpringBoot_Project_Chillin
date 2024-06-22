package com.chillin.controller;

import com.chillin.dto.BoardDTO;
import com.chillin.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /*

        @GetMapping("/editor_template")
        public String editorTemplate() {
            return "board/editor_template";
        }
    */
    @GetMapping("/community/create")
    public String createBoard() {
        return "board/community_create";
    }

    @PostMapping("/editor/image")
    @ResponseBody
    public Map<String, Object> editorImage(@RequestParam("upload") MultipartFile image) {

        System.out.println(filePath);
        Map<String, Object> data = new HashMap<>();
        if (image != null) {
            data = boardService.fileUpload(filePath, image);
        }
        return data;
    }

    @GetMapping("/getImage/{file_name}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(
            @PathVariable(name = "file_name") String fileName) {

        InputStream in = null;
        ResponseEntity<byte[]> entity = null;
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        try {
            in = new FileInputStream(filePath + "/" + fileName);
            HttpHeaders headers = new HttpHeaders();
            entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(in)
                    , headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println("getImage 에서 에러" + e);
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return entity;
    }

    @PostMapping("/board/create_board")
    @ResponseBody
    public BoardDTO createBoard(@RequestBody BoardDTO dto
            , HttpSession session) {
        System.out.println("\n\n\n======================");
        System.out.println(dto.getTitle());
        System.out.println(dto.getContent());
        System.out.println("======================\n\n\n");

        Long uid = (Long) session.getAttribute("uid");

        if (uid == null) {
            dto.setSuccess(false);
        } else {
            dto.setUid(uid);
            boolean success = boardService.insertBoard(dto);
            dto.setSuccess(success);
        }

        return dto;
    }


    @GetMapping("/community/{board_id}")
    public String detailBoard(@PathVariable("board_id") Long bid, Model model) {
        BoardDTO dto = boardService.getDetail(bid);
        model.addAttribute("board", dto);
        return "board/community_detail";
    }

    @GetMapping("/community/modify/{board_id}")
    public String modifyBoard(@PathVariable("board_id") Long bid
            , Model model
            , HttpSession session) {

        Long uid = (Long) session.getAttribute("uid");

        BoardDTO dto = boardService.getDetail(bid);
        if (uid == null || !uid.equals(dto.getUid()) || dto.getUid() != uid) {
            return "redirect:/community/" + bid;
        }

        model.addAttribute("board", dto);
        return "board/community_modify";
    }

    @PostMapping("/community/modify/{board_id}")
    @ResponseBody
    public BoardDTO modifyBoard(@PathVariable("board_id") Long bid
            , @RequestBody BoardDTO dto
            , HttpSession session) {

        Long uid = (Long) session.getAttribute("uid");

        BoardDTO originBoard = boardService.getDetail(bid);
        if(uid == null || !uid.equals(originBoard.getUid()) || uid != originBoard.getUid()){
            dto.setSuccess(false);
        }else{
            dto.setBid(bid);
            //String id = "testid";
            dto.setUid(uid);
            boolean success = boardService.modifyBoard(dto);
            dto.setSuccess(success);
        }

        return dto;
    }

    @GetMapping("/community/delete/{board_id}")
    public String deleteBoard(@PathVariable("board_id") Long bid, HttpSession session) {

        Long uid = (Long) session.getAttribute("uid");

        BoardDTO originBoard = boardService.getDetail(bid);
        if(uid == null || !uid.equals(originBoard.getUid()) || uid != originBoard.getUid()){
            return "redirect:/community/"+bid;
        }else{
            boardService.delete(bid);
        }
        return "redirect:/community/list";
    }


}

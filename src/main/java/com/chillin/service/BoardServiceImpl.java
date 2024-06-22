package com.chillin.service;

import com.chillin.domain.Board;
import com.chillin.domain.User;
import com.chillin.dto.BoardDTO;
import com.chillin.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    @Override
    public Map<String, Object> fileUpload(String filePath, MultipartFile image) {
        Map<String, Object> data = new HashMap<>();

        if(image != null){
            String originalName = image.getOriginalFilename();
            String fileName = uploading(filePath,image);

            data.put("uploaded", 1);
            data.put("fileName", fileName);
            data.put("url", "http://localhost:8080/getImage/" + fileName);
        }

        return data;
    }

    @Override
    public boolean insertBoard(BoardDTO dto) {

        boolean result = false;

        User user = boardRepository.getUser(dto.getUid());
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
        Board saved = boardRepository.save(board);

        Long bid = saved.getBoardId();
        if(bid > 0){
            dto.setBid(bid);
            dto.setWriteDate(saved.getWriteDate());

            result = true;
        }
        return result;
    }

    @Override
    public BoardDTO getDetail(Long bid) {

        Optional<Board> optionalBoard = boardRepository.findById(bid);
        Board find = optionalBoard.orElseThrow(()-> new RuntimeException());
        BoardDTO dto = BoardDTO.builder()
                .bid(find.getBoardId())
                .title(find.getTitle())
                .content(find.getContent())
                .writeDate(find.getWriteDate())
                .modifyDate(find.getModifyDate())
                .uid(find.getUser().getUserId())
                .nickname(find.getUser().getNickname())
                .build();
        return dto;
    }

    @Override
    public void delete(Long bid, String id) {
        /*id 에서 uid 갖고와야함*/
        Long uid = 2l;

        Board board = boardRepository.findById(bid).orElseThrow(()->new RuntimeException());

        if(uid.equals(board.getUser().getUserId())){
            boardRepository.delete(board);
        }

    }

    @Override
    public void delete(Long bid) {
        Board board = boardRepository.findById(bid).orElseThrow(()->new RuntimeException());
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public boolean modifyBoard(BoardDTO dto) {

        boolean result = false;

        Long uid = dto.getUid();
        Board prev = boardRepository.findById(dto.getBid())
                .orElseThrow(RuntimeException::new);

        if(uid.equals(prev.getUser().getUserId())){
            prev.setTitle(dto.getTitle());
            prev.setContent(dto.getContent());
            boardRepository.save(prev);

            result = true;
        }

        return result;
    }

    private String uploading(String filePath, MultipartFile image) {

        UUID uuid = UUID.randomUUID();
        String fileName = image.getOriginalFilename();

        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+","%20");

        fileName = uuid + "_" + fileName;

        File save = new File(filePath,fileName);

        try {
                image.transferTo(save);
        } catch (IOException e) {
                save.delete();
            throw new RuntimeException();
        }
        return fileName;
    }
}

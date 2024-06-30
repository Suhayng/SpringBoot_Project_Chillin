package com.chillin.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.chillin.config.S3Config;
import com.chillin.domain.Board;
import com.chillin.domain.BoardBoom;
import com.chillin.domain.User;
import com.chillin.dto.BoardDTO;
import com.chillin.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final S3Config s3Config;

    /**
     * 첫 String 은 sessionId, Set의 String 은 이미지 url
     */
    private final Map<String, Set<String>> imgMap = new HashMap<>();

    @Value("${cloud.aws.s3.bucket}")
    String bucket;

    @Override
    public Map<String, Object> fileUpload(String filePath, MultipartFile image, String sessionId) {
        Map<String, Object> data = new HashMap<>();
        Set<String> mySet;
        if (imgMap.containsKey(sessionId)) {
            mySet = imgMap.get(sessionId);
        } else {
            mySet = new HashSet<>();
            imgMap.put(sessionId, mySet);
        }

        if (image != null) {
            String originalName = image.getOriginalFilename();
            //String fileName = uploading(filePath, image);
            File localFile = uploading2(filePath, image);
            String fileName = localFile.getName();

            //String fileLoc = "http://localhost:8080/getImage/"+fileName;
            PutObjectResult result = s3Config.amazonS3Client()
                    .putObject(new PutObjectRequest(bucket, fileName, localFile));
            String s3Url = s3Config.amazonS3Client().getUrl(bucket, fileName).toString();

            mySet.add(fileName);

            data.put("uploaded", 1);
            data.put("fileName", fileName);
            //data.put("url", "http://localhost:8080/getImage/" + fileName);
            data.put("url", s3Url);


        }

        return data;
    }

    @Override
    public boolean insertBoard(BoardDTO dto, String sessionId) {

        deleteNonInsert(dto.getContent(), sessionId);
        imgMap.remove(sessionId);

        boolean result = false;

        User user = boardRepository.getUser(dto.getUid());
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .blind(false)
                .build();
        Board saved = boardRepository.save(board);

        Long bid = saved.getBoardId();
        if (bid > 0) {
            dto.setBid(bid);
            dto.setWriteDate(saved.getWriteDate());

            result = true;
        }
        return result;
    }

    private Set<String> parsedImg(String content) {
        Set<String> set = new HashSet<>();
        String[] parsing1 = content.split("<img src=\"");

        String prefix = "https://kdt-java5-1.s3.ap-northeast-2.amazonaws.com/";
        for (int i = 0; i < parsing1.length; i++) {
            String[] parse2 = parsing1[i].split("\">");
            for (int j = 0; j < parse2.length; j++) {
                if (parse2[j].contains(prefix)) {
                    String parse3 = parse2[j].split(prefix)[1];
                    set.add(parse3);
                }
            }
        }
        return set;
    }

    private void deleteNonInsert(String content, String sessionId) {
        Set<String> saved = parsedImg(content);

        Set<String> uploaded = imgMap.get(sessionId);
        Iterator<String> uploadedIta = uploaded.iterator();
        while (uploadedIta.hasNext()) {
            String target = uploadedIta.next();
            /*contain이 제대로 일을 안해서 for문 돌아야될 거 같음*/
            Iterator<String> savedIta = saved.iterator();
            boolean isDelete = true;
            while (savedIta.hasNext()) {
                String savedOne = savedIta.next().replace("%25", "%");
                if (savedOne.equals(target)) {
                    isDelete = false;
                    break;
                }
            }
            if (isDelete) s3Config.amazonS3Client().deleteObject(bucket, target);
            /*if(!saved.contains(target)){
                //S3 삭제 작업
                s3Config.amazonS3Client().deleteObject(bucket,target);
            }*/
        }
    }

    @Override
    public BoardDTO getDetail(Long bid) {

        Optional<Board> optionalBoard = boardRepository.findById(bid);
        Board find = optionalBoard.orElseThrow(() -> new RuntimeException());
        BoardDTO dto = BoardDTO.builder()
                .bid(find.getBoardId())
                .title(find.getTitle())
                .content(find.getContent())
                .writeDate(find.getWriteDate())
                .modifyDate(find.getModifyDate())
                .uid(find.getUser().getUserId())
                .nickname(find.getUser().getNickname())
                .blind(find.getBlind())
                .build();
        return dto;
    }
/*
    @Override
    public void delete(Long bid, String id) {
        *//*id 에서 uid 갖고와야함*//*
        Long uid = 2l;

        Board board = boardRepository.findById(bid).orElseThrow(() -> new RuntimeException());

        if (uid.equals(board.getUser().getUserId())) {
            boardRepository.delete(board);
        }

    }*/

    @Override
    public void delete(Long bid) {
        Board board = boardRepository.findById(bid).orElseThrow(() -> new RuntimeException());
        boardRepository.delete(board);

        deleteImg(board.getContent());

    }

    private void deleteImg(String content) {
        Set<String> imgs = parsedImg(content);
        Iterator<String> imgsIta = imgs.iterator();
        while (imgsIta.hasNext()){
            String toDeleteImg = imgsIta.next().replace("%25","%");
            s3Config.amazonS3Client().deleteObject(bucket, toDeleteImg);
        }
    }

    @Override
    @Transactional
    public boolean modifyBoard(BoardDTO dto, String sessionId) {
        /*걍 시작할라고.. ;;*/
        boolean result = false;

        Long uid = dto.getUid();
        Board prev = boardRepository.findById(dto.getBid())
                .orElseThrow(RuntimeException::new);

        if (uid.equals(prev.getUser().getUserId())) {
            String prevContent = prev.getContent();
            String nowContent = dto.getContent();

            prev.setTitle(dto.getTitle());
            prev.setContent(nowContent);
            boardRepository.save(prev);

            /*
                prev와 now의 비교를 통한 삭제할 이미지 삭제하기
            */
            deleteModifyingImg(prevContent, nowContent, sessionId);


            result = true;
        }

        return result;
    }

    private void deleteModifyingImg(String prevContent, String nowContent, String sessionId) {
        Set<String> prev = parsedImg(prevContent);
        Set<String> now = parsedImg(nowContent);
        Set<String> uploading = imgMap.get(sessionId);

        /* 예전 글에는 있었다가 삭제된거 */
        Iterator<String> prevIta = prev.iterator();
        while (prevIta.hasNext()) {
            String prevImg = prevIta.next();
            Iterator<String> nowIta = now.iterator();
            boolean isDelete = true;
            while (nowIta.hasNext()) {
                String nowImg = nowIta.next();
                if (nowImg.equals(prevImg)) {
                    isDelete = false;
                    break;
                }
            }
            if (isDelete) {
                String deleteKey = prevImg.replace("%25", "%");
                s3Config.amazonS3Client().deleteObject(bucket, deleteKey);
            }
        }
        /* 이제 막 올렸는데 빠꾸친거 */
        if (uploading != null) {
            Iterator<String> uploadingIta = uploading.iterator();
            while (uploadingIta.hasNext()) {
                String uploadingImg = uploadingIta.next();
                Iterator<String> nowIta = now.iterator();
                boolean isDelete = true;
                while (nowIta.hasNext()) {
                    String nowImg = nowIta.next().replace("%25", "%");
                    if (uploadingImg.equals(nowImg)) {
                        isDelete = false;
                        break;
                    }
                }
                if (isDelete) s3Config.amazonS3Client().deleteObject(bucket, uploadingImg);
            }
        }
        imgMap.remove(sessionId);
    }

    @Override
    public Map<String, Object> getBoom(Long uid, Long bid) {
        Map<String, Object> map = boardRepository.getBoardBoom(bid);

        if (uid == null) {
            map.put("status", "no");
        } else {
            Boolean myBoom = false;
            Object boomTest = null;

            //myBoom = boardRepository.boardMyBoom(uid,bid);
            boomTest = boardRepository.boardMyBoom(uid, bid);

            if (boomTest == null) {
                map.put("status", "no");
            } else {
                myBoom = (Boolean) boomTest;

                if (myBoom == true) map.put("status", "up");
                if (myBoom == false) map.put("status", "down");
            }
        }


        return map;
    }

    @Override
    @Transactional
    public Map<String, Object> boomupBoard(Long uid, Long bid, String status) {
        Map<String, Object> map = null;

        if (status.equals("no")) {
            /*insert -> 1*/
            boardRepository.insertBoom(true, bid, uid);

            map = boardRepository.getBoardBoom(bid);
            map.put("status", "up");
        } else if (status.equals("up")) {
            /*delete*/
            boardRepository.deleteBoom(bid, uid);
            map = boardRepository.getBoardBoom(bid);
            map.put("status", "no");

        } else if (status.equals("down")) {
            /*update -> 1*/
            boardRepository.changeDown(true, bid, uid);
            map = boardRepository.getBoardBoom(bid);
            map.put("status", "up");
        } else {
            map = boardRepository.getBoardBoom(bid);
            map.put("status", "fail");
        }
        return map;
    }

    @Override
    @Transactional
    public Map<String, Object> boomdownBoard(Long uid, Long bid, String status) {
        Map<String, Object> map = null;
        if (status.equals("no")) {
            /*insert -> 0*/
            boardRepository.insertBoom(false, bid, uid);
            map = boardRepository.getBoardBoom(bid);
            map.put("status", "down");
        } else if (status.equals("down")) {
            /*delete*/
            boardRepository.deleteBoom(bid, uid);
            map = boardRepository.getBoardBoom(bid);
            map.put("status", "no");
        } else if (status.equals("up")) {
            boardRepository.changeDown(false, bid, uid);
            map = boardRepository.getBoardBoom(bid);
            map.put("status", "down");
            /*update -> 0*/
        } else {
            map = boardRepository.getBoardBoom(bid);
            map.put("status", "fail");
        }
        return map;
    }

    @Override
    public String isBookmarked(Long uid, Long bid) {

        Long bookmarkObject = boardRepository.isBookmarked(uid, bid);
        if (bookmarkObject == null) {
            return "no";
        } else {
            if (bookmarkObject > 0) {
                return "yes";
            } else {
                return "no";
            }
        }
    }

    @Override
    public String bookmaring(Long uid, Long bid, String status) {
        if ("no".equals(status)) {
            /*yes 로 return 하고 , insert 를 보냄 */
            boardRepository.insertBookmark(bid, uid);
            return "yes";
        } else if ("yes".equals(status)) {
            /*no 로 return 하고 , delete 를 보냄 */
            boardRepository.deleteBookmark(bid, uid);
            return "no";
        } else {
            return "fail";
        }
    }

    @Override
    public List<BoardDTO> getRecentList(String search, int iPage, int pageSize) {
        int startRow = (iPage - 1) * pageSize;
        List<BoardDTO> recentList
                = boardRepository.getRecentList(search, startRow, pageSize);

        return recentList;
    }

    @Override
    public List<BoardDTO> getDayList() {
        return boardRepository.getDayList();
    }

    @Override
    public List<BoardDTO> getWeekList() {
        return boardRepository.getWeekList();
    }

    @Override
    public Long getTotalPage(String search, int pageSize) {
        Long getTotalBoard = boardRepository.getTotalBoard(search);
        Long pageNum = getTotalBoard / pageSize + (getTotalBoard % pageSize == 0 ? 0 : 1);

        return pageNum;
    }

    @Override
    public List<BoardDTO> getUserBoard(String nickname) {

        return boardRepository.getUserBoard(nickname);
    }

    private String uploading(String filePath, MultipartFile image) {

        UUID uuid = UUID.randomUUID();
        String fileName = image.getOriginalFilename();

        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        fileName = uuid + "_" + fileName;

        File save = new File(filePath, fileName);

        try {
            image.transferTo(save);
        } catch (IOException e) {
            save.delete();
            throw new RuntimeException();
        }
        return fileName;
    }

    private File uploading2(String filePath, MultipartFile image) {

        UUID uuid = UUID.randomUUID();
        String fileName = image.getOriginalFilename();

        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        fileName = uuid + "_" + fileName;

        File save = new File(filePath, fileName);

        try {
            image.transferTo(save);
        } catch (IOException e) {
            save.delete();
            throw new RuntimeException();
        }
        return save;
    }


}

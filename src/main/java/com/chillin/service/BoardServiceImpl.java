package com.chillin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BoardServiceImpl implements BoardService{


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

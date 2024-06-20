package com.chillin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BoardServiceImpl implements BoardService{


    @Override
    public Map<String, Object> fileUpload(String filePath, MultipartFile image) {
        Map<String, Object> data = new HashMap<String, Object>();

        if(image != null){
            String originalName = image.getOriginalFilename();
            String fName = uploading(filePath,image);

            data.put("uploaded", 1);
            data.put("fileName", originalName);
            //data.put("url", "http://localhost:8123" + imgPath);
        }

        return data;
    }

    private String uploading(String filePath, MultipartFile image) {
        File save = null;

        UUID uuid = UUID.randomUUID();
        String fileName = image.getOriginalFilename();

        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replace("+","%20");

        System.out.println(fileName);

        return "any";
    }
}

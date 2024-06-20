package com.chillin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BoardService {
    Map<String, Object> fileUpload(String filePath, MultipartFile image);
}

package com.chillin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@MappedSuperclass
public class BoardBase {
    private String title;
    private String content;
    @CreatedDate
    @Column(name = "write_date")
    private LocalDateTime writeDate;
}

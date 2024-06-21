package com.chillin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Setter
@Getter
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid")
    private Long newsId;

    private String link;
    private String title;
    private String publisher;
    @CreatedDate
    @Column(name = "write_date")
    private LocalDateTime writeDate;

}

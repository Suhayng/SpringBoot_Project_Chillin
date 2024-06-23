package com.chillin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@NoArgsConstructor
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

    @Builder
    public News(String link, String title, String publisher, LocalDateTime writeDate){
        this.link = link;
        this.title = title;
        this.publisher = publisher;
        this.writeDate = writeDate;
    }

}

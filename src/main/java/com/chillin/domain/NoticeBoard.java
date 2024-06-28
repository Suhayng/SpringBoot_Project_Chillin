package com.chillin.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Setter
@Getter
@NoArgsConstructor
public class NoticeBoard extends BoardBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nbid")
    private Long noticeId;

    @Builder
    public NoticeBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "notice")
public class NoticeBoard extends BoardBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nbid")
    private Long noticeId;

}

package com.chillin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notice")
@Setter
@Getter
public class NoticeBoard extends BoardBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nbid")
    private Long noticeId;

}

package com.chillin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board_boom")
@Setter
@Getter
public class BoardBoom{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boomid")
    private Long boardBoomId;

    @Column(name = "updown")
    private Boolean upDown;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    private Board board;
}

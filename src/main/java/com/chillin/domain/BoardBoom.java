package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "board_boom")
public class BoardBoom extends BoomBase{

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

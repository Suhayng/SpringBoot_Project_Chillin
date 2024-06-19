package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "board_boom")
public class BoardBoom extends BoomBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boomid")
    private Long boardBoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    private Board board;
}

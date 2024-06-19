package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "board")
public class Board extends BoardBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

}

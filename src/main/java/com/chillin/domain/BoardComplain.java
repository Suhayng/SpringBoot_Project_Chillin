package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "board_complain")
public class BoardComplain extends ComplainBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bcid")
    private Long boardComplainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    private Board board;


}

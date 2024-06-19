package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rep_boom")
public class RepBoom extends BoomBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rboomid")
    private Long repBoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rid")
    private Rep rep;
}

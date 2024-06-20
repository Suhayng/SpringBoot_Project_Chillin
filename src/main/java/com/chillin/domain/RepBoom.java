package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rep_boom")
public class RepBoom extends BoomBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rboomid")
    private Long repBoomId;

    @Column(name = "updown")
    private Boolean upDown;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rid")
    private Rep rep;
}

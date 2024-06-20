package com.chillin.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rep_complain")
public class RepComplain extends ComplainBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rcid")
    private Long repComplainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rid")
    private Rep rep;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

}

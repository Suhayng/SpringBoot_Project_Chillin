package com.chillin.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rep_boom")
@Setter
@Getter
public class RepBoom{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rboomid")
    private Long repBoomId;

    @Column(name = "updown")
    private Boolean upDown;

    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rid")
    private Rep rep;
}

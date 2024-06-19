package com.chillin.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Long userId;

    private String id;
    private String password;
    private String nickname;
    private String name;

}

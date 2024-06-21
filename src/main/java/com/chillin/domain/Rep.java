package com.chillin.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rep")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@NoArgsConstructor
public class Rep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid")
    private Long repId;

    private String content;

    @CreatedDate
    @Column(name = "write_date")
    private LocalDateTime writeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @OneToMany(mappedBy = "rep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepBoom> repBoomList = new ArrayList<>();

    @OneToMany(mappedBy = "rep", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepComplain> repComplainList = new ArrayList<>();

    @Builder
    public Rep(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }
}

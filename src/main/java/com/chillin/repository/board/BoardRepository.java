package com.chillin.repository.board;

import com.chillin.domain.Board;
import com.chillin.domain.User;
import com.chillin.repository.user.UserQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDSL {

    @Query("select u from User u where u.userId = :uid")
    User getUser(Long uid);

    @Query(" select u.userId from User u where u.id= :id ")
    Long getUserId(String id);


/*
    @Query(" select bb.upDown from BoardBoom bb " +
            " where bb.board.boardId =:bid and bb.user.userId =:uid ")
    Boolean boardMyBoom(Long uid, Long bid);*/
}

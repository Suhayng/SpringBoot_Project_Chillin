package com.chillin.repository.rep;

import com.chillin.domain.Board;
import com.chillin.domain.Rep;
import com.chillin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepRepository extends JpaRepository<Rep, Long>, RepQueryDSL{

    @Query("select u from User u where u.id =:suid")
    User getUserBySuid(String suid);

    @Query("select b from Board b where b.boardId =:bid")
    Board getBoardByBid(Long bid);
}

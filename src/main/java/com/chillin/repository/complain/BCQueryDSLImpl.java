package com.chillin.repository.complain;


import static com.chillin.domain.QBoardComplain.*;

import com.chillin.domain.BoardComplain;
import static com.chillin.domain.QRepComplain.*;

import com.chillin.domain.RepComplain;
import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.RepComplainDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class BCQueryDSLImpl implements BCQueryDSL{

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean prevBoardWrite(Long bid, Long uid) {
        List<BoardComplain> list = queryFactory.select(boardComplain)
                .from(boardComplain)
                .where(boardComplain.board.boardId.eq(bid)
                        .and(boardComplain.user.userId.eq(uid)))
                .fetch();
        if(list.size() > 0) return true;
        else return false;

    }

    @Override
    @Transactional
    @Modifying
    public boolean write(BoardComplainDTO dto, Long uid, boolean completed) {

        Long bid = dto.getBid();

        String insertSql = null;
        if(!completed){
            /* 5, 10 이면 블라인드 걸게 count 하는 쿼리 받아오기 */
            insertSql= "insert into board_complain(uid,bid,complete,reason,detail) " +
                    " values(:uid , :bid , 0 , :reason , :detail)";
        }else{
            insertSql= "insert into board_complain(uid,bid,complete,reason,detail) " +
                    " values(:uid , :bid , 1, :reason, :detail)";
        }


        Query query = entityManager.createNativeQuery(insertSql);
        query.setParameter("uid", uid);
        query.setParameter("bid", bid);
        query.setParameter("reason", dto.getReason());
        query.setParameter("detail", dto.getDetail());
        query.executeUpdate();

        if(!completed){
            Long complainNum = countComplain(bid);
            if(complainNum % 5 == 0){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }

    }

    @Override
    @Transactional
    @Modifying
    public boolean write(RepComplainDTO dto, Long uid, boolean completed) {
        Long rid = dto.getRid();

        String insertSql = null;
        if(!completed){
            /* 5, 10 이면 블라인드 걸게 count 하는 쿼리 받아오기 */
            insertSql= "insert into rep_complain(uid,rid,complete,reason,detail) " +
                    " values(:uid , :rid , 0 , :reason , :detail)";
        }else{
            insertSql= "insert into rep_complain(uid,rid,complete,reason,detail) " +
                    " values(:uid , :rid , 1, :reason, :detail)";
        }


        Query query = entityManager.createNativeQuery(insertSql);
        query.setParameter("uid", uid);
        query.setParameter("rid", rid);
        query.setParameter("reason", dto.getReason());
        query.setParameter("detail", dto.getDetail());
        query.executeUpdate();

        if(!completed){
            Long complainNum = countRepComplain(rid);
            if(complainNum % 5 == 0){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }
    @Override
    public boolean prevRepWrite(Long rid, Long uid) {


        List<RepComplain> list = queryFactory.select(repComplain)
            .from(repComplain)
            .where(repComplain.rep.repId.eq(rid).and(repComplain.user.userId.eq(uid)))
            .fetch();
        if(list.size() > 0) return true;
        else return false;
    }

    private Long countComplain(Long bid) {
        return queryFactory.select(boardComplain.count())
                .from(boardComplain)
                .where(boardComplain.board.boardId.eq(bid)
                        .and(boardComplain.complete.eq(false)))
                .fetchOne();
    }


    private Long countRepComplain(Long rid) {
        return queryFactory.select(repComplain.count())
                .from(repComplain)
                .where(repComplain.rep.repId.eq(rid)
                        .and(repComplain.complete.eq(false)))
                .fetchOne();
    }

}

package com.chillin.repository.complain;


import static com.chillin.domain.QBoardComplain.*;

import com.chillin.domain.BoardComplain;

import static com.chillin.domain.QRepComplain.*;

import static com.chillin.domain.QBoard.*;

import static com.chillin.domain.QUser.*;

import static com.chillin.domain.QRep.*;

import com.chillin.domain.RepComplain;
import com.chillin.dto.BoardComplainDTO;
import com.chillin.dto.ComplainManageDTO;
import com.chillin.dto.RepComplainDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BCQueryDSLImpl implements BCQueryDSL {

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
        if (list.size() > 0) return true;
        else return false;

    }

    @Override
    @Transactional
    @Modifying
    public boolean write(BoardComplainDTO dto, Long uid, boolean completed) {

        Long bid = dto.getBid();

        String insertSql = null;
        if (!completed) {
            /* 5, 10 이면 블라인드 걸게 count 하는 쿼리 받아오기 */
            insertSql = "insert into board_complain(uid,bid,complete,reason,detail) " +
                    " values(:uid , :bid , 0 , :reason , :detail)";
        } else {
            insertSql = "insert into board_complain(uid,bid,complete,reason,detail) " +
                    " values(:uid , :bid , 1, :reason, :detail)";
        }


        Query query = entityManager.createNativeQuery(insertSql);
        query.setParameter("uid", uid);
        query.setParameter("bid", bid);
        query.setParameter("reason", dto.getReason());
        query.setParameter("detail", dto.getDetail());
        query.executeUpdate();

        if (!completed) {
            Long complainNum = countComplain(bid);
            if (complainNum % 5 == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    @Transactional
    @Modifying
    public boolean write(RepComplainDTO dto, Long uid, boolean completed) {
        Long rid = dto.getRid();

        String insertSql = null;
        if (!completed) {
            /* 5, 10 이면 블라인드 걸게 count 하는 쿼리 받아오기 */
            insertSql = "insert into rep_complain(uid,rid,complete,reason,detail) " +
                    " values(:uid , :rid , 0 , :reason , :detail)";
        } else {
            insertSql = "insert into rep_complain(uid,rid,complete,reason,detail) " +
                    " values(:uid , :rid , 1, :reason, :detail)";
        }


        Query query = entityManager.createNativeQuery(insertSql);
        query.setParameter("uid", uid);
        query.setParameter("rid", rid);
        query.setParameter("reason", dto.getReason());
        query.setParameter("detail", dto.getDetail());
        query.executeUpdate();

        if (!completed) {
            Long complainNum = countRepComplain(rid);
            if (complainNum % 5 == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean prevRepWrite(Long rid, Long uid) {


        List<RepComplain> list = queryFactory.select(repComplain)
                .from(repComplain)
                .where(repComplain.rep.repId.eq(rid).and(repComplain.user.userId.eq(uid)))
                .fetch();
        if (list.size() > 0) return true;
        else return false;
    }

    @Override
    public List<ComplainManageDTO> getBoardList(int startRow, int pageSize, String type) {

        BooleanExpression searchCondition = null;

        if (type.equals("blind")) {
            searchCondition = board.blind.eq(true);
        } else if (type.equals("open")) {
            searchCondition = board.blind.eq(false);
        } else if (type.equals("complete")) {
            searchCondition = boardComplain.complete.eq(true);
        } else if (type.equals("non_complete")) {
            searchCondition = boardComplain.complete.eq(false);
        }

        List<ComplainManageDTO> list = queryFactory.select(
                        Projections.fields(
                                ComplainManageDTO.class
                                , boardComplain.boardComplainId.as("cid")
                                , board.boardId.as("target")
                                , board.title
                                , board.blind
                                , user.userId.as("uid")
                                , user.nickname
                                , boardComplain.reason
                                , boardComplain.detail
                                , boardComplain.complete))
                .from(boardComplain)
                .innerJoin(board)
                .on(boardComplain.board.boardId.eq(board.boardId))
                .fetchJoin()
                .innerJoin(user)
                .on(board.user.userId.eq(user.userId))
                .fetchJoin()
                .where(searchCondition)
                .offset(startRow).limit(pageSize)
                .fetch();

        return list;
    }

    @Override
    public List<ComplainManageDTO> getBoardUnionList(Long bid) {

        List<ComplainManageDTO> list = queryFactory.select(
                        Projections.fields(
                                ComplainManageDTO.class
                                , boardComplain.boardComplainId.as("cid")
                                , board.boardId.as("target")
                                , board.title
                                , board.blind
                                , user.userId.as("uid")
                                , user.nickname
                                , boardComplain.reason
                                , boardComplain.detail
                                , boardComplain.complete))
                .from(boardComplain)
                .innerJoin(board)
                .on(boardComplain.board.boardId.eq(board.boardId))
                .fetchJoin()
                .innerJoin(user)
                .on(board.user.userId.eq(user.userId))
                .fetchJoin()
                .where(board.boardId.eq(bid))
                .fetch();

        return list;
    }

    @Override
    public List<ComplainManageDTO> getRepUnionList(Long cid) {

        Long rid = queryFactory.select(repComplain.rep.repId)
                .from(repComplain)
                .where(repComplain.repComplainId.eq(cid))
                .fetchOne();

        List<ComplainManageDTO> list = queryFactory.select(
                        Projections.fields(
                                ComplainManageDTO.class
                                , repComplain.repComplainId.as("cid")
                                , rep.board.boardId.as("target")
                                , rep.content.as("title")
                                , rep.blind
                                , user.userId.as("uid")
                                , user.nickname
                                , repComplain.reason
                                , repComplain.detail
                                , repComplain.complete))
                .from(repComplain)
                .innerJoin(rep)
                .on(repComplain.rep.repId.eq(rep.repId))
                .fetchJoin()
                .innerJoin(user)
                .on(rep.user.userId.eq(user.userId))
                .fetchJoin()
                .where(rep.repId.eq(rid))
                .fetch();

        return list;
    }

    @Override
    @Transactional
    @Modifying
    public void repBlindingRid(Long rid) {
        queryFactory.update(rep)
                .set(rep.blind,true)
                .where(rep.repId.eq(rid))
                .execute();
    }

    @Override
    @Transactional
    @Modifying
    public void blinding(Long bid, String action) {
        boolean setting = false;
        if (action.equals("do")) {
            setting = true;
        }
        queryFactory.update(board)
                .set(board.blind, setting)
                .where(board.boardId.eq(bid))
                .execute();
    }

    @Override
    @Transactional
    @Modifying
    public void completing(Long cid) {

        queryFactory.update(boardComplain)
                .set(boardComplain.complete, true)
                .where(boardComplain.boardComplainId.eq(cid))
                .execute();
    }

    @Override
    public List<ComplainManageDTO> getRepList(int startRow, int pageSize, String type) {

        BooleanExpression searchCondition = null;

        if (type.equals("blind")) {
            searchCondition = rep.blind.eq(true);
        }else if (type.equals("open")) {
            searchCondition = rep.blind.eq(false);
        } else if (type.equals("complete")) {
            searchCondition = repComplain.complete.eq(true);
        } else if (type.equals("non_complete")) {
            searchCondition = repComplain.complete.eq(false);
        }

        List<ComplainManageDTO> list = queryFactory.select(
                        Projections.fields(
                                ComplainManageDTO.class
                                , repComplain.repComplainId.as("cid")
                                , board.boardId.as("target")
                                , rep.content.as("title")
                                , rep.blind
                                , rep.repId.as("rid")
                                , user.userId.as("uid")
                                , user.nickname
                                , repComplain.reason
                                , repComplain.detail
                                , repComplain.complete))
                .from(repComplain)
                .innerJoin(rep)
                .on(repComplain.rep.repId.eq(rep.repId))
                .fetchJoin()
                .innerJoin(user)
                .on(rep.user.userId.eq(user.userId))
                .fetchJoin()
                .innerJoin(board)
                .on(rep.board.boardId.eq(board.boardId))
                .fetchJoin()
                .where(searchCondition)
                .offset(startRow).limit(pageSize)
                .fetch();

        return list;
    }

    @Override
    @Modifying
    @Transactional
    public void repBlinding(Long cid, String action) {
        boolean setting = false;
        if (action.equals("do")) {
            setting = true;
        }
        Long rid = queryFactory.select(repComplain.rep.repId)
                .from(repComplain)
                .where(repComplain.repComplainId.eq(cid))
                .fetchOne();

        queryFactory.update(rep)
                .set(rep.blind, setting)
                .where(rep.repId.eq(rid))
                .execute();
    }

    @Override
    @Modifying
    @Transactional
    public void repCompleting(Long cid) {

        queryFactory.update(repComplain)
                .set(repComplain.complete, true)
                .where(repComplain.repComplainId.eq(cid))
                .execute();
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

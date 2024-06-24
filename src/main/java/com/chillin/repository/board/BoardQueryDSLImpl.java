package com.chillin.repository.board;

import com.chillin.domain.BoardBoom;

import static com.chillin.domain.QBoardBoom.*;

import static com.chillin.domain.QBookmark.*;

import com.chillin.dto.RepDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chillin.domain.QRep.rep;

@RequiredArgsConstructor
public class BoardQueryDSLImpl implements BoardQueryDSL {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public List<RepDTO> getReps(Long bid) {

        List<RepDTO> dtoList =
                queryFactory.select(Projections.fields(
                                RepDTO.class
                                , rep.repId.as("rid")
                                , rep.content
                                , rep.writeDate
                                , rep.user.userId.as("uid")
                                , rep.board.boardId.as("bid")
                                , rep.user.nickname
                        )).from(rep)
                        .where(rep.board.boardId.eq(bid))
                        .fetch();
        return dtoList;
    }

    @Override
    public Boolean boardMyBoom(Long uid, Long bid) {

        Boolean result = null;
        try {
            result = queryFactory.select(boardBoom.upDown)
                    .from(boardBoom)
                    .where(boardBoom.board.boardId.eq(bid)
                            .and(boardBoom.user.userId.eq(uid)))
                    .fetchOne();
        } catch (NullPointerException e) {
            System.out.println(e);
        }

        return result;
    }

    @Override
    @Transactional
    @Modifying
    public void insertBoom(Boolean updown, Long bid, Long uid) {
        String sql = "INSERT INTO board_boom (uid, bid, updown) VALUES (:uid, :bid, :updown)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("uid", uid);
        query.setParameter("bid", bid);
        query.setParameter("updown", updown);
        query.executeUpdate();
    }

    @Override
    @Transactional
    @Modifying
    public void deleteBoom(Long bid, Long uid) {
        queryFactory.delete(boardBoom)
                .where(boardBoom.board.boardId.eq(bid)
                        .and(boardBoom.user.userId.eq(uid)))
                .execute();
    }

    @Override
    @Transactional
    @Modifying
    public void changeDown(boolean boom, Long bid, Long uid) {
        queryFactory.update(boardBoom)
                .set(boardBoom.upDown, boom)
                .where(boardBoom.board.boardId.eq(bid)
                        .and(boardBoom.user.userId.eq(uid)))
                .execute();
    }

    @Override
    @Transactional
    public Map<String, Object> getBoardBoom(Long bid) {
        Map<String, Object> map = new HashMap<>();

        Tuple tuple = queryFactory.select(
                        boardBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                        , boardBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown"))
                .from(boardBoom)
                .where(boardBoom.board.boardId.eq(bid))
                .fetchOne();

        Integer boomup = tuple.get(0, Integer.class);
        if (boomup == null) boomup = 0;
        Integer boomdown = tuple.get(1, Integer.class);
        if (boomdown == null) boomdown = 0;

        map.put("boomup", boomup);
        map.put("boomdown", boomdown);

        return map;
    }

    @Override
    public Long isBookmarked(Long uid, Long bid) {

        Long result = queryFactory.select(bookmark.count())
                .from(bookmark)
                .where(bookmark.board.boardId.eq(bid).and(bookmark.user.userId.eq(uid)))
                .fetchOne();

        return result;
    }

    @Override
    @Transactional
    @Modifying
    public void insertBookmark(Long bid, Long uid) {
        String sql = "INSERT INTO bookmark(bid,uid) VALUES (:bid , :uid)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("bid", bid);
        query.setParameter("uid", uid);
        query.executeUpdate();
    }

    @Override
    @Transactional
    @Modifying
    public void deleteBookmark(Long bid, Long uid) {
        queryFactory.delete(bookmark)
                .where(bookmark.board.boardId.eq(bid)
                        .and(bookmark.user.userId.eq(uid)))
                .execute();
    }
}

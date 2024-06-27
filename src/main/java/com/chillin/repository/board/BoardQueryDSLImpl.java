package com.chillin.repository.board;

import com.chillin.domain.Board;
import com.chillin.domain.BoardBoom;

import static com.chillin.domain.QBoardBoom.*;
import static com.chillin.domain.QBoardComplain.boardComplain;
import static com.chillin.domain.QBookmark.*;
import static com.chillin.domain.QBoard.*;
import static com.chillin.domain.QUser.*;

import com.chillin.dto.BoardDTO;
import com.chillin.dto.RepDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chillin.domain.QRep.rep;
import static com.chillin.domain.QRepBoom.repBoom;
import static com.chillin.domain.QBookmark.*;

@RequiredArgsConstructor
@Slf4j
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

    @Override
    public List<BoardDTO> getRecentList(String search, int startRow, int pageSize) {

        BooleanExpression searchCondition = null;
        if(search != null){
            searchCondition = board.title.contains(search);
        }

        List<BoardDTO> list =
                queryFactory.select(Projections.fields(
                            BoardDTO.class
                            ,board.boardId.as("bid")
                            ,board.title
                            ,board.content
                            ,board.writeDate
                            ,board.user.userId.as("uid")
                            ,board.user.nickname))
                        .from(board)
                        .innerJoin(user)
                        .on(board.user.userId.eq(user.userId))
                        .where(searchCondition)
                        .orderBy(board.writeDate.desc())
                        .offset(startRow).limit(pageSize)
                        .fetch();

        list.stream().forEach(boardDTO -> {
            /* 붐업 붐따 가져오기 */
            Tuple tuple = queryFactory.select(
                            boardBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                            , boardBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
                    )
                    .from(board)
                    .leftJoin(boardBoom)
                    .on(board.boardId.eq(boardBoom.board.boardId))
                    .where(boardBoom.board.boardId.eq(boardDTO.getBid()))
                    .fetchOne();
            Integer boomup = tuple.get(0,Integer.class);
            if(boomup == null) boomup = 0;
            Integer boomdown = tuple.get(1,Integer.class);
            if(boomdown == null) boomdown = 0;
            boardDTO.setBoomup(boomup);
            boardDTO.setBoomdown(boomdown);
        });

        return list;
    }

    @Override
    public Long getTotalBoard(String search) {

        BooleanExpression searchCondition = null;
        if(search != null){
            searchCondition = board.title.contains(search);
        }
        Long totalBoardCount = queryFactory.select(board.count())
                .from(board)
                .where(searchCondition)
                .fetchOne();

        return totalBoardCount;
    }

    @Override
    public List<BoardDTO> getDayList() {


        List<Tuple> bid_boomsum = queryFactory.select(
                boardBoom.board.boardId
                ,boardBoom.upDown.when(true).then(1)
                        .otherwise(-3).sum().as("boomsum"))
                .from(boardBoom)
                .innerJoin(board)
                .on(boardBoom.board.boardId.eq(board.boardId))
                .where(board.writeDate.after(LocalDateTime.now().minusDays(1)))
                .groupBy(boardBoom.board.boardId)
                .orderBy(boardBoom.upDown.when(true).then(1)
                        .otherwise(-3).sum().desc()
                ,board.writeDate.desc())
                .offset(0).limit(5)
                .fetch();

        List<BoardDTO> dayList = new ArrayList<>();
        for(int i = 0 ; i <bid_boomsum.size() ; i++) {
            dayList.add(
            queryFactory.select(
                            Projections.fields(
                                    BoardDTO.class
                                    , board.boardId.as("bid")
                                    , board.title
                                    , board.writeDate
                                    , board.user.userId.as("uid")
                                    , board.user.nickname
                            ))
                    .from(board)
                    .where(board.boardId.eq(bid_boomsum.get(i).get(0, Long.class)))
                    .offset(0).limit(5)
                    .fetchOne()
            );
        }
        dayList.stream().forEach(boardDTO -> {
            Tuple tuple = queryFactory.select(
                            boardBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                            , boardBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
                    )
                    .from(boardBoom)
                    .where(boardBoom.board.boardId.eq(boardDTO.getBid()))
                    .fetchOne();
            boardDTO.setBoomup(tuple.get(0,Integer.class));
            boardDTO.setBoomdown(tuple.get(1,Integer.class));
        });

        return dayList;
    }

    @Override
    public List<BoardDTO> getWeekList() {

        List<Tuple> bid_boomsum = queryFactory.select(
                        boardBoom.board.boardId
                        ,boardBoom.upDown.when(true).then(1)
                                .otherwise(-3).sum().as("boomsum"))
                .from(boardBoom)
                .innerJoin(board)
                .on(boardBoom.board.boardId.eq(board.boardId))
                .where(board.writeDate.after(LocalDateTime.now().minusDays(7)))
                .groupBy(boardBoom.board.boardId)
                .orderBy(boardBoom.upDown.when(true).then(1)
                        .otherwise(-3).sum().desc()
                        ,board.writeDate.desc())
                .offset(0).limit(5)
                .fetch();

        List<BoardDTO> weekList = new ArrayList<>();
        for(int i = 0 ; i <bid_boomsum.size() ; i++){
            weekList.add(
                    queryFactory.select(
                                    Projections.fields(
                                            BoardDTO.class
                                            ,board.boardId.as("bid")
                                            ,board.title
                                            ,board.writeDate
                                            ,board.user.userId.as("uid")
                                            ,board.user.nickname
                                    ))
                            .from(board)
                            .where(board.boardId.eq(bid_boomsum.get(i).get(0,Long.class)))
                            .offset(0).limit(5)
                            .fetchOne()
            );
        }


        weekList.stream().forEach(boardDTO -> {
            Tuple tuple = queryFactory.select(
                            boardBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                            , boardBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
                    )
                    .from(boardBoom)
                    .where(boardBoom.board.boardId.eq(boardDTO.getBid()))
                    .fetchOne();
            boardDTO.setBoomup(tuple.get(0,Integer.class));
            boardDTO.setBoomdown(tuple.get(1,Integer.class));
        });

        return weekList;
    }

    /**북마크한 리스트 가져오기*/
    public List<BoardDTO> getBookmarkList(Long uid){
        List<BoardDTO> list =
                queryFactory.select(Projections.fields(
                                BoardDTO.class
                                ,board.boardId.as("bid")
                                ,board.title
                                ,board.content
                                ,board.writeDate
                                ,board.user.userId.as("uid")
                                ,board.user.nickname))
                        .from(bookmark)
                        .innerJoin(board)
                        .on(bookmark.board.boardId.eq(board.boardId))
                        .where(bookmark.user.userId.eq(uid))
                        .orderBy(board.writeDate.desc())
                        .fetch();

        return list;
    }

    /**내가 쓴 글 리스트 가져오기*/
    @Override
    public List<BoardDTO> getMyBoardList(Long uid) {

        List<BoardDTO> list = queryFactory.select(Projections.fields(
                        BoardDTO.class
                        , board.boardId.as("bid")
                        , board.title
                        , board.content
                        , board.writeDate
                        , board.user.userId.as("uid")
                        , board.user.nickname))
                .from(board)
                .where(board.user.userId.eq(uid))
                .orderBy(board.writeDate.desc())
                .fetch();
        return list;
    }

    /** 회원이 쓴 글 조회 */
    @Override
    public List<BoardDTO> getUserBoardList(Long uid) {

        List<Tuple> fetch = queryFactory.select(
                          board.boardId
                        , board.title
                        , board.writeDate
                        , board.modifyDate
                        , boardBoom.upDown.count())
                .from(board)
                .innerJoin(boardBoom).on(board.boardId.eq(boardBoom.board.boardId))
                .where(board.user.userId.eq(uid))
                .groupBy(board.title, board, board.writeDate, board.modifyDate)
                .fetch();

        List<BoardDTO> boardDTOS = new ArrayList<>();

        for (Tuple tuple : fetch) {
            Long boardId = tuple.get(board.boardId);
            String title = tuple.get(board.title);
            LocalDateTime writeDate = tuple.get(board.writeDate);
            LocalDateTime modifyDate = tuple.get(board.modifyDate);
            Long updownCount = tuple.get(boardBoom.upDown.count());

            // 서브쿼리로 신고수 가져오기
            Long complainCount = queryFactory.select(boardComplain.count())
                    .from(boardComplain)
                    .where(boardComplain.board.boardId.eq(boardId))
                    .fetchOne();

            BoardDTO dto = new BoardDTO();
            dto.setBid(boardId);
            dto.setTitle(title);
            dto.setWriteDate(writeDate);
            dto.setModifyDate(modifyDate);
            dto.setUpDownCount(updownCount);
            dto.setComplainCount(complainCount);

            log.info("board...{}", boardId);
            log.info("title...{}", title);
            log.info("writedate...{}", writeDate);
            log.info("modifyDate...{}", modifyDate);
            log.info("updownCount...{}", updownCount);
            log.info("complainCount...{}", complainCount);

            boardDTOS.add(dto);
        }

        return boardDTOS;
    }


}

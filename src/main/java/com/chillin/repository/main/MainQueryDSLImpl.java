package com.chillin.repository.main;

import com.chillin.dto.BoardDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.chillin.domain.QBoard.board;
import static com.chillin.domain.QBoardBoom.boardBoom;
import static com.chillin.domain.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class MainQueryDSLImpl implements MainQueryDSL{

    private final JPAQueryFactory queryFactory;


    /** 커뮤니티 최신 리스트 */
    @Override
    public List<BoardDTO> getMainBoardList() {

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
                        .orderBy(board.writeDate.desc())
                        .offset(0).limit(5)
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


}

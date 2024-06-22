package com.chillin.repository.board;

import com.chillin.domain.BoardBoom;
import static com.chillin.domain.QBoardBoom.*;
import com.chillin.dto.RepDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.chillin.domain.QRep.rep;

@RequiredArgsConstructor
public class BoardQueryDSLImpl implements BoardQueryDSL{

    private final JPAQueryFactory queryFactory;

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
        try{
            result = queryFactory.select(boardBoom.upDown)
                    .from(boardBoom)
                    .where(boardBoom.board.boardId.eq(bid)
                            .and(boardBoom.user.userId.eq(uid)))
                    .fetchOne();

            System.out.println("repository 에서 result : "+ result);
        }catch (NullPointerException e){
            System.out.println("그래도 이 캐치에는 왔어");
            System.out.println(e);
        }

        return result;
    }
}

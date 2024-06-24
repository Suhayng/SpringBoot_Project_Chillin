package com.chillin.repository.rep;

import static com.chillin.domain.QBoardBoom.boardBoom;
import static com.chillin.domain.QRep.*;

import static com.chillin.domain.QRepBoom.*;

import com.chillin.domain.QRepBoom;
import com.chillin.domain.Rep;
import com.chillin.dto.RepDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RepQueryDSLImpl implements RepQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
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
    public List<RepDTO> getReps2(Long bid, Long uid) {
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
                        .leftJoin(repBoom)
                        .on(rep.repId.eq(repBoom.rep.repId))
                        .fetchJoin()
                        .where(rep.board.boardId.eq(bid))
                        .fetch();

        QRepBoom repBoom1 = new QRepBoom("repBoom1");
        List<RepDTO> boomRepList = dtoList.stream()
                .map(repDTO -> {

                    Tuple tuple = queryFactory.select(
                                    repBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                                    , repBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
                                    , JPAExpressions.select(repBoom1.user.userId.eq(uid).count())
                                            .from(repBoom1)
                                            .where(repBoom1.rep.repId.eq(repDTO.getRid()))
                            )
                            .from(repBoom)
                            .where(repBoom.rep.repId.eq(repDTO.getRid()))
                            .fetchOne();

                    Tuple

                    return repDTO;
                }).collect(Collectors.toList());

        /*
                                , repBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                                , repBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
                                , repBoom.user.userId.when(uid)
                                        .then(repBoom.upDown.when(true).then("up").otherwise("down"))
                                        .otherwise("no").as("status")
        * */

        return dtoList;
    }
}

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
                        /*.leftJoin(repBoom) *//* 영속성으로 쿼리가 덜 날라가지 않을까.. 하는 마음*//*
                        .on(rep.repId.eq(repBoom.rep.repId))
                        .fetchJoin()*/
                        .where(rep.board.boardId.eq(bid))
                        .fetch();

        dtoList.stream().forEach(repDTO ->{
            Tuple tuple = queryFactory.select(
                            repBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                            , repBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
                    )
                    .from(repBoom)
                    .where(repBoom.rep.repId.eq(repDTO.getRid()))
                    .fetchOne();

            String status = queryFactory.select(
                            repBoom.upDown.when(true).then("up")
                                    .otherwise("down")
                    ).from(repBoom)
                    .where(repBoom.rep.repId.eq(repDTO.getRid()).
                            and(repBoom.user.userId.eq(uid)))
                    .fetchOne();

            Integer boomup = tuple.get(0,Integer.class);
            if(boomup == null) boomup = 0;
            repDTO.setBoomup(boomup);

            Integer boomdown = tuple.get(1,Integer.class);
            if(boomdown == null) boomdown = 0;
            repDTO.setBoomdown(boomdown);
            if(status == null) status = "no";
            repDTO.setStatus(status);
        });

        return dtoList;
    }
}
  /*
  , repBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
  , repBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
  , repBoom.user.userId.when(uid)
  .then(repBoom.upDown.when(true).then("up").otherwise("down"))
  .otherwise("no").as("status")
        * */


        /*List<RepDTO> boomRepList = dtoList.stream()
                .map(repDTO -> {

                    Tuple tuple = queryFactory.select(
                                    repBoom.upDown.when(true).then(1).otherwise(0).sum().as("boomup")
                                    , repBoom.upDown.when(false).then(1).otherwise(0).sum().as("boomdown")
                            )
                            .from(repBoom)
                            .where(repBoom.rep.repId.eq(repDTO.getRid()))
                            .fetchOne();

                    String status = queryFactory.select(
                                    repBoom.upDown.when(true).then("up")
                                            .otherwise("down")
                            ).from(repBoom)
                            .where(repBoom.rep.repId.eq(repDTO.getRid()).
                                    and(repBoom.user.userId.eq(uid)))
                            .fetchOne();
                    repDTO.setBoomup(tuple.get(0,Integer.class));
                    repDTO.setBoomdown(tuple.get(1,Integer.class));
                    if(status == null) status = "no";
                    repDTO.setStatus(status);
                    return repDTO;
                }).collect(Collectors.toList());
*/
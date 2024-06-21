package com.chillin.repository.rep;

import static com.chillin.domain.QRep.*;

import com.chillin.domain.Rep;
import com.chillin.dto.RepDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
                                , rep.user.id
                        )).from(rep)
                        .where(rep.board.boardId.eq(bid))
                        .fetch();
        return dtoList;
    }
}

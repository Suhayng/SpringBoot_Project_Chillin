package com.chillin.repository.message;

import com.chillin.domain.Message;
import com.chillin.dto.MessageDTO;
import com.chillin.dto.RepDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import static com.chillin.domain.QMessage.*;
import static com.chillin.domain.QRep.rep;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class MessageQueryDSLImpl implements MessageQueryDSL {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Message> getMessageList(Long userId){

        String sql = "  SELECT m.meId, m.content, m.time, m.is_read, m.sender, m.receiver        " +
                "       FROM message m   " +
                "       JOIN (           " +
                "              SELECT                             " +
                "                     CASE WHEN sender = :userId THEN receiver " +
                "                          ELSE sender            " +
                "                          END AS other_user_id,  " +
                "                     MAX(time) AS max_sent_time  " +
                "               FROM message                      " +
                "               WHERE sender = :userId OR receiver = :userId    " +
                "               GROUP BY                          " +
                "                     CASE WHEN sender = :userId THEN receiver  " +
                "                          ELSE sender            " +
                "                          END                    " +
                "             ) latest ON        " +
                "                            (m.sender = :userId                    " +
                "                            AND m.receiver = latest.other_user_id  " +
                "                            AND m.time = latest.max_sent_time)     " +
                "                         OR     " +
                "                            (m.receiver = :userId                   " +
                "                            AND m.sender = latest.other_user_id     " +
                "                            AND m.time = latest.max_sent_time)      ";

        Query query = entityManager.createNativeQuery(sql, Message.class)
                .setParameter("userId", userId);
        List<Message> resultList = query.getResultList();

        return resultList;
    }

    @Override
    public List<Message> getMessageDetailList(Long userId, Long messageId) {

        List<Message> messageList = queryFactory.select(message)
                .from(message)
                .where(message.sender.userId.eq(userId).and(message.receiver.userId.eq(messageId))
                        .or(message.sender.userId.eq(messageId).and(message.receiver.userId.eq(userId))))
                .orderBy(message.time.desc())
                .fetch();

        return messageList;
    }


}

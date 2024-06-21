package com.chillin.repository.user;

import com.chillin.domain.QUser;
import com.chillin.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.chillin.domain.QUser.*;


@RequiredArgsConstructor
@Slf4j
public class UserQueryDSLImpl implements UserQueryDSL{

    private final JPAQueryFactory queryFactory;

    @Override
    public User findByEmail(String id) {
        User user = queryFactory.select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.id.eq(id))
                .fetchOne();

        return user;
    }

    @Override
    public Long findByNickName(String nickName) {
        Long result = queryFactory.select(user.count())
                .from(user)
                .where(user.nickname.eq(nickName))
                .fetchOne();
        return result;
    }
}

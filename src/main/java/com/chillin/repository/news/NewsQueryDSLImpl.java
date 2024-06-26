package com.chillin.repository.news;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewsQueryDSLImpl implements NewsBoardQueryDSL{

    private final JPAQueryFactory queryFactory;

}

package com.chillin.repository;

import com.chillin.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Override
    <S extends News> List<S> saveAll(Iterable<S> entities);

    /** 뉴스 링크 존재 확인 */
    boolean existsByLink(String link);

}

package com.chillin.repository.news;

import com.chillin.domain.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsBoardQueryDSL {

    @Override
    <S extends News> List<S> saveAll(Iterable<S> entities);

    /** 뉴스 링크 존재 확인 */
    boolean existsByLink(String link);

    /** 뉴스 목록 (검색, 페이징) */
    Page<News> findByTitleContaining(String title, Pageable pageable);

    @Query(" select n.title, n.link " +
            " from News n " +
            " order by n.newsId desc")
    List<News> mainNewsList();
}
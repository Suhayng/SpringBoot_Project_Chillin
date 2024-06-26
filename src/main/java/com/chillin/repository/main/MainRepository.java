package com.chillin.repository.main;

import com.chillin.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainRepository extends JpaRepository<Board, Long>, MainQueryDSL {
}

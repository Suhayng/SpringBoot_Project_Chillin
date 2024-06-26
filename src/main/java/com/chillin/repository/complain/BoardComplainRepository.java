package com.chillin.repository.complain;

import com.chillin.domain.BoardComplain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardComplainRepository extends JpaRepository<BoardComplain, Long>
        , BCQueryDSL{


}

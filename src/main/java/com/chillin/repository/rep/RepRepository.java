package com.chillin.repository.rep;

import com.chillin.domain.Rep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepRepository extends JpaRepository<Rep, Long>, RepQueryDSL{

}

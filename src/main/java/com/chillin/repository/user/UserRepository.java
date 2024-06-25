package com.chillin.repository.user;

import com.chillin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserQueryDSL{

    @Override
    <S extends User> S save(S entity);

    @Override
    void deleteById(Long aLong);
}

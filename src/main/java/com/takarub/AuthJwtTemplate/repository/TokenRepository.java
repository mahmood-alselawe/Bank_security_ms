package com.takarub.AuthJwtTemplate.repository;

import com.takarub.AuthJwtTemplate.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {

    @Query("""
    select t from Token t
    inner join t.user u
    where u.id = :id and t.expired = false and t.revoked = false
    """)
    List<Token> findAllValidTokenByUser(Integer id);


    Optional<Token> findByToken(String token);
}

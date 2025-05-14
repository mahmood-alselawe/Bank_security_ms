package com.takarub.AuthJwtTemplate.repository;

import com.takarub.AuthJwtTemplate.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.passWord = ?2 WHERE u.email = ?1")
    void updatePassword(String email, String password);
}

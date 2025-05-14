package com.takarub.AuthJwtTemplate.repository;
import com.takarub.AuthJwtTemplate.model.ForgotPassword;
import com.takarub.AuthJwtTemplate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = ?1 AND fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
    ForgotPassword findByUser(User user);
}

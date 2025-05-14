package com.takarub.AuthJwtTemplate.repository;

import com.takarub.AuthJwtTemplate.model.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findTopByAccountNumberOrderByCreatedAtDesc(String accountNumber);
    void deleteByAccountNumber(String accountNumber);


}

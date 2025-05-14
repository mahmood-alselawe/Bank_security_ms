package com.takarub.AuthJwtTemplate.repository;

import com.takarub.AuthJwtTemplate.model.BankModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankModelRepository extends JpaRepository<BankModel, Long> {
    Optional<BankModel> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    BankModel findByAccountNumber(String accountNumber);

}

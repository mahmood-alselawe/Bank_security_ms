package com.takarub.AuthJwtTemplate.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Data
@Entity
@Table(name = "Transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEBIT or CREDIT

    private String status; // e.g., SUCCESS, FAILED

    private String performedBy;

    private String note;

    private LocalDateTime timestamp;
}

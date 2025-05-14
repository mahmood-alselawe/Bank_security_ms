package com.takarub.AuthJwtTemplate.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue
    private Integer id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

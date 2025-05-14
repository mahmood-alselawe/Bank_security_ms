package com.takarub.AuthJwtTemplate.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Data
public class ForgotPassword {

    @Id
    @GeneratedValue
    private Integer fpId;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date exirationDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}

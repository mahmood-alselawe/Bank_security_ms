package com.takarub.AuthJwtTemplate.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@ToString(exclude = "tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_User")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String passWord;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Token> tokens;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ForgotPassword forgotPassword;
    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.name()));
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return passWord;
    }

    @Override
    public String getUsername() {
        return email;
    }


}

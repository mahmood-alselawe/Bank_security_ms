package com.takarub.AuthJwtTemplate.service;

import com.takarub.AuthJwtTemplate.dto.changePasswordRequest;
import com.takarub.AuthJwtTemplate.model.User;
import com.takarub.AuthJwtTemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserServices {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void changePassword(changePasswordRequest request, Principal connectedUser) {
        var user = (User)((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(),user.getPassword())){
            throw new IllegalStateException("wrong password");
            // create new custom exception for this and handle it
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("password are not the same");
            // create new custom exception for this and handle it
        }


        user.setPassWord(passwordEncoder.encode(request.getNewPassword()));


        userRepository.save(user);


    }
}


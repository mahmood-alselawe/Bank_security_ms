package com.takarub.AuthJwtTemplate.service;


import com.takarub.AuthJwtTemplate.dto.ChangePasswordForgot;
import com.takarub.AuthJwtTemplate.dto.MailBody;
import com.takarub.AuthJwtTemplate.model.ForgotPassword;
import com.takarub.AuthJwtTemplate.model.User;
import com.takarub.AuthJwtTemplate.repository.ForgotPasswordRepository;
import com.takarub.AuthJwtTemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotService {

    private final UserRepository userRepository;

    private final ForgotPasswordRepository forgotPasswordRepository;

    private final EmailServiceImpl mailService;

    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> verifyEmail(String email){
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("please provide a valid email"));

        int otp = otpGenerator();
        MailBody mailBody = MailBody
                .builder()
                .to(email)
                .text("This is the OTP for forgot password request: " + otp)
                .subject("OTP for forgot password request")
                .build();

        // Check for existing ForgotPassword record
        ForgotPassword existingFp = forgotPasswordRepository.findByUser(user);
        if (existingFp != null) {
            // Optionally, update the existing record
            existingFp.setOtp(otp);

            existingFp.setExirationDate(new Date(System.currentTimeMillis() + 100 * 1000));
            forgotPasswordRepository.save(existingFp);
        } else {
            // Create a new record if none exists
            ForgotPassword fp = ForgotPassword
                    .builder()
                    .otp(otp)
                    .exirationDate(new Date(System.currentTimeMillis() + 100 * 1000))
                    .user(user)
                    .build();
            forgotPasswordRepository.save(fp);
        }

        mailService.sendSimpleMessage(mailBody);
        return ResponseEntity.ok("Email sent for verification");
    }
    public ResponseEntity<String> verifyOtp( Integer otp,
                                             String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("please provide a valid email"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email " + email));

        if (fp.getExirationDate().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpId());
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }

    public ResponseEntity<String> changePasswordHandler (ChangePasswordForgot changePassword,
                                                         String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again", HttpStatus.EXPECTATION_FAILED);
        }

        String encode = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encode);

        return ResponseEntity.ok("Password has been changed");
    }



    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}

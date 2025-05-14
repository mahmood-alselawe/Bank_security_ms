package com.takarub.AuthJwtTemplate.service.bankImpl;

import com.takarub.AuthJwtTemplate.dto.MailBody;
import com.takarub.AuthJwtTemplate.model.BankModel;
import com.takarub.AuthJwtTemplate.model.OtpEntity;
import com.takarub.AuthJwtTemplate.repository.BankModelRepository;
import com.takarub.AuthJwtTemplate.repository.OtpRepository;
import com.takarub.AuthJwtTemplate.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final BankModelRepository bankModelRepository;
    private final EmailServiceImpl emailService;

    public String generateOtpForApi(String accountNumber) {
        BankModel account = bankModelRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw  new RuntimeException("Account not found");
        }
        String otp  = generateOtp(accountNumber);

        emailService.sendSimpleMessage(
                MailBody.builder()
                        .subject("Your OTP Code")
                        .to(account.getUser().getEmail())
                        .text("Use this OTP to confirm your transaction: " + otp)
                        .build()
        );
        return "OTP sent to registered email.";


    }


    private String generateOtp(String accountNumber) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setAccountNumber(accountNumber);
        otpEntity.setOtpCode(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(3));
        otpRepository.save(otpEntity);
        return otp;
    }

    public boolean verifyOtp(String accountNumber, String otp) {
        Optional<OtpEntity> optionalOtp = otpRepository.findTopByAccountNumberOrderByCreatedAtDesc(accountNumber);
        if (optionalOtp.isPresent()) {
            OtpEntity savedOtp = optionalOtp.get();
            boolean isValid = savedOtp.getOtpCode().equals(otp) &&
                    LocalDateTime.now().isBefore(savedOtp.getExpiresAt());
            return isValid;
        }
        return false;
    }

    public void clearOtp(String accountNumber) {
        otpRepository.deleteByAccountNumber(accountNumber);
    }


}

package com.takarub.AuthJwtTemplate.service.bankImpl;
import com.takarub.AuthJwtTemplate.dto.MailBody;
import com.takarub.AuthJwtTemplate.dto.bankDto.*;
import com.takarub.AuthJwtTemplate.exception.AccountLockedException;
import com.takarub.AuthJwtTemplate.exception.BankAccountAlreadyExistsException;
import com.takarub.AuthJwtTemplate.model.BankModel;
import com.takarub.AuthJwtTemplate.model.TransactionLog;
import com.takarub.AuthJwtTemplate.model.TransactionType;
import com.takarub.AuthJwtTemplate.model.User;
import com.takarub.AuthJwtTemplate.repository.BankModelRepository;
import com.takarub.AuthJwtTemplate.repository.UserRepository;
import com.takarub.AuthJwtTemplate.service.BankService;
import com.takarub.AuthJwtTemplate.service.EmailServiceImpl;
import com.takarub.AuthJwtTemplate.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {

    private final BankModelRepository bankModelRepository;

    private final UserRepository userRepository;

    private final EmailServiceImpl emailService;

    private final OtpService otpService;

    private final TransactionLogService transactionLogService;
    @Override
    public BankResponse createBankAccount(String email,BankAccountRequest request) {

        User byEmail = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found with this email : " + email ));


        if (bankModelRepository.existsByUserEmail(email)) {
            throw new BankAccountAlreadyExistsException("User already has a bank account");
        }

        BankModel bankModel = BankModel.builder()
                .otherName(request.getOtherName())
                .gender(request.getGender())
                .address(request.getAddress())
                .stateOfOrigin(request.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status(request.getStatus())
                .user(byEmail)
                .locked(false)
                .lockReason(null)
                .build();

        BankModel saveAfterCreated = bankModelRepository.save(bankModel);
        emailService.sendSimpleMessage(MailBody.builder()
                .subject("Account created")
                .to(saveAfterCreated.getUser().getEmail())
                .text("You have successfully created a bank account.")
                .build()
        );

        return BankResponse
                .builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(
                        AccountInfo
                                .builder()
                                .accountBalance(saveAfterCreated.getAccountBalance())
                                .accountNumber(saveAfterCreated.getAccountNumber())
                                .accountName(saveAfterCreated.getUser().getFirstName() + " " + saveAfterCreated.getUser().getLastName() + " " + saveAfterCreated.getOtherName())
                                .build()
                )
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(String accountNumber) {
        BankModel account = getAccountOrThrow(accountNumber);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountName(getFullName(account))
                        .accountNumber(accountNumber)
                        .accountBalance(account.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(String accountNumber) {
        BankModel account = getAccountOrThrow(accountNumber);
        return getFullName(account);
    }
    @Override
    @Transactional
    public BankResponse depositToAccount(CreditDebitRequest request) {

        // Validate account existence
        BankModel account = bankModelRepository.findByAccountNumber(request.getAccountNumber());
        if (account == null) {
            throw new RuntimeException("Account with account number: " + request.getAccountNumber() + " does not exist.");
        }

        if (account.isLocked()) {
            throw new AccountLockedException("Account is locked: " + account.getLockReason());
        }
        if (!otpService.verifyOtp(request.getAccountNumber(), request.getOtp())){
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        // Update account balance
        account.setAccountBalance(account.getAccountBalance().add(request.getAmount()));
        BankModel updatedAccount = bankModelRepository.save(account);


        // Log receiver (credit)
        transactionLogService.logTransaction(TransactionLog.builder()
                .accountNumber(account.getAccountNumber())
                .amount(request.getAmount())
                .type(TransactionType.CREDIT)
                .status("SUCCESS")
                .performedBy(account.getUser().getUsername()) // You can also use "SYSTEM" or "TRANSFER"
                .note("Received transfer from account: " + account.getAccountNumber())
                .timestamp(LocalDateTime.now())
                .build());

        // ✅ Clear OTP after use
        otpService.clearOtp(request.getAccountNumber());
        // Send email notification
        emailService.sendSimpleMessage(MailBody.builder()
                .subject("Deposit Successful")
                .to(updatedAccount.getUser().getEmail())
                .text("Dear " + getFullName(updatedAccount) + ",\n\n" +
                        "Your deposit of " + request.getAmount() + " was successful. " +
                        "Your new balance is " + updatedAccount.getAccountBalance() + ".\n\n" +
                        "Thank you for banking with us.")
                .build()
        );

        // Build and return response
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountName(getFullName(updatedAccount))
                        .accountNumber(updatedAccount.getAccountNumber())
                        .accountBalance(updatedAccount.getAccountBalance())
                        .build())
                .build();
    }
    @Override
    @Transactional
    public BankResponse debitAccount(CreditDebitRequest request) {

        boolean isAccountExists = bankModelRepository
                .existsByAccountNumber(request.getAccountNumber());

        validateDebitRequest(request);


        BankModel account = bankModelRepository.findByAccountNumber(request.getAccountNumber());


        if (account == null) {
            throw new IllegalArgumentException("Account with number " + request.getAccountNumber() + " does not exist.");
        }

        if (account.isLocked()) {
            throw new AccountLockedException("Account is locked: " + account.getLockReason());
        }

        // Check balance
        if (request.getAmount().compareTo(account.getAccountBalance()) > 0) {
            log.warn("Insufficient funds for account: {}", request.getAccountNumber());
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Perform debit
        account.setAccountBalance(account.getAccountBalance().subtract(request.getAmount()));
        BankModel updatedAccount = bankModelRepository.save(account);


        transactionLogService.logTransaction(TransactionLog.builder()
                .accountNumber(request.getAccountNumber())
                .amount(request.getAmount())
                .type(TransactionType.DEBIT)
                .status("SUCCESS")
                .performedBy(account.getUser().getUsername()) // or ID
                .note("Debited from account")
                .timestamp(LocalDateTime.now())
                .build());

        // Send notification
        emailService.sendSimpleMessage(MailBody.builder()
                .subject("Account Debit Notification")
                .to(account.getUser().getEmail())
                .text("Dear Customer, your account has been debited with an amount of " + request.getAmount() +
                        ". Your new balance is: " + account.getAccountBalance())
                .build()
        );

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(request.getAccountNumber())
                        .accountName(getFullName(updatedAccount))
                        .accountBalance(updatedAccount.getAccountBalance())
                        .build())
                .build();



    }
    @Override
    @Transactional
    public BankResponse transfer(TransFerRequest transFerRequest) {

        validateTransferRequest(transFerRequest);

        // Fetch source and destination accounts
        BankModel sourceAccount = bankModelRepository.findByAccountNumber(transFerRequest.getSourceAccountNumber());
        BankModel destinationAccount = bankModelRepository.findByAccountNumber(transFerRequest.getDestinationAccountNumber());

        // Check if both accounts exist
        if (sourceAccount == null) {
            throw new IllegalArgumentException("Source account does not exist: " + transFerRequest.getSourceAccountNumber());
        }

        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account does not exist: " + transFerRequest.getDestinationAccountNumber());
        }
        if (sourceAccount.isLocked()) {
            throw new AccountLockedException("Account is locked: " + sourceAccount.getLockReason());
        }
        if (destinationAccount.isLocked()) {
            throw new AccountLockedException("Account is locked: " + destinationAccount.getLockReason());
        }

        // Check sufficient funds
        if (transFerRequest.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient funds in source account");
        }
        if (!otpService.verifyOtp(transFerRequest.getSourceAccountNumber(), transFerRequest.getOtp())){
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        // Perform transfer
        log.info("Transferring amount {} from {} to {}", transFerRequest.getAmount(),
                sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber());

        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transFerRequest.getAmount()));
        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transFerRequest.getAmount()));

        bankModelRepository.save(sourceAccount);
        bankModelRepository.save(destinationAccount);

        transactionLogService.logTransaction(TransactionLog.builder()
                .accountNumber(sourceAccount.getAccountNumber())
                .amount(transFerRequest.getAmount())
                .type(TransactionType.DEBIT)
                .status("SUCCESS")
                .performedBy(sourceAccount.getUser().getUsername())
                .note("Transfer to account: " + destinationAccount.getAccountNumber())
                .timestamp(LocalDateTime.now())
                .build());

        transactionLogService.logTransaction(TransactionLog.builder()
                .accountNumber(destinationAccount.getAccountNumber())
                .amount(transFerRequest.getAmount())
                .type(TransactionType.CREDIT)
                .status("SUCCESS")
                .performedBy(sourceAccount.getUser().getUsername()) // or destinationAccount.getUser().getUsername()
                .note("Received transfer from account: " + sourceAccount.getAccountNumber())
                .timestamp(LocalDateTime.now())
                .build());

        otpService.clearOtp(transFerRequest.getSourceAccountNumber());
        // Send notifications
        emailService.sendSimpleMessage(
                MailBody.builder()
                        .subject("DEPOSIT ALERT")
                        .to(sourceAccount.getUser().getEmail())
                        .text("The sum of " + transFerRequest.getAmount() + " has been credited to your account. Current balance: " + sourceAccount.getAccountBalance())
                        .build()
        );
        emailService.sendSimpleMessage(
                MailBody.builder()
                        .subject("DEPOSIT ALERT")
                        .to(destinationAccount.getUser().getEmail())
                        .text("The sum of " + transFerRequest.getAmount() + " has been credited to your account. Current balance: " + destinationAccount.getAccountBalance())
                        .build()
        );
        return BankResponse.builder()
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .accountInfo(null) // You can include transaction details or account info here
                .build();
    }
    @Override
    public String lockAccount(String accountNumber, String reason) {
        BankModel byAccountNumber = bankModelRepository.findByAccountNumber(accountNumber);

        if (byAccountNumber == null) {
            return "Account " + accountNumber + " not found. Unable to lock the account.";
        }

        byAccountNumber.setLocked(true);
        byAccountNumber.setLockReason(reason);
        byAccountNumber.setLockTimestamp(LocalDateTime.now());
        bankModelRepository.save(byAccountNumber);

        emailService.sendSimpleMessage(
                MailBody
                        .builder()
                        .subject("LOCK ACCOUNT")
                        .text("Your account has been locked for the following reason: " + reason)
                        .to(byAccountNumber.getUser().getEmail())
                        .build()
        );

        return "Account " + accountNumber + " has been successfully locked for the reason: " + reason;
    }
    @Override
    public String unlockAccount(String accountNumber) {
        BankModel account = bankModelRepository.findByAccountNumber(accountNumber);
        if (account == null || !account.isLocked()) {
            return "Account is not locked or does not exist.";
        }

        account.setLocked(false);
        account.setLockReason(null);
        account.setLockTimestamp(null);
        bankModelRepository.save(account);

        emailService.sendSimpleMessage(
                MailBody.builder()
                        .subject("UNLOCK ACCOUNT")
                        .text("Your account has been unlocked.")
                        .to(account.getUser().getEmail())
                        .build()
        );

        return "Account " + accountNumber + " has been unlocked.";
    }

    private void validateTransferRequest(TransFerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Transfer request is null");
        }

        if (request.getSourceAccountNumber() == null || request.getDestinationAccountNumber() == null) {
            throw new IllegalArgumentException("Source and destination account numbers are required");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        if (request.getSourceAccountNumber().equals(request.getDestinationAccountNumber())) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }
    }

    private void validateDebitRequest(CreditDebitRequest request) {
        if (request == null || request.getAccountNumber() == null || request.getAmount() == null) {
            throw new IllegalArgumentException("Invalid debit request. Account number and amount are required.");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount to debit must be greater than zero.");
        }
    }

    private String getFullName(BankModel account) {
        return account.getUser().getFirstName() + " " +
                account.getUser().getLastName() + " " +
                (account.getOtherName() != null ? account.getOtherName() : "");
    }
    private BankModel getAccountOrThrow(String accountNumber) {
        return Optional.ofNullable(bankModelRepository.findByAccountNumber(accountNumber))
                .orElseThrow(() -> new RuntimeException("Account with account number " + accountNumber + " does not exist."));
    }





}

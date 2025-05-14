package com.takarub.AuthJwtTemplate.exception;

public class BankAccountAlreadyExistsException extends RuntimeException{
    public BankAccountAlreadyExistsException(String message) {
        super(message);
    }
}

package com.takarub.AuthJwtTemplate.exception;

public class AccountLockedException extends RuntimeException{
    public AccountLockedException(String message){
        super(message);
    }
}

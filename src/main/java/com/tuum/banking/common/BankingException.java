package com.tuum.banking.common;

public class BankingException extends RuntimeException {
    public BankingException() {
        super();
    }

    public BankingException(String message) {
        super(message);
    }
}

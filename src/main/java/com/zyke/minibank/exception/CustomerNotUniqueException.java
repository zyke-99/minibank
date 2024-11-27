package com.zyke.minibank.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotUniqueException extends MinibankException {
    public CustomerNotUniqueException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}

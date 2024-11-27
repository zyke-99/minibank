package com.zyke.minibank.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends MinibankException {
    public CustomerNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}

package com.zyke.minibank.exception;

import org.springframework.http.HttpStatus;

public class AddressOwnershipException extends MinibankException {
    public AddressOwnershipException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}

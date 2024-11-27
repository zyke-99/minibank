package com.zyke.minibank.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Value
public class MinibankException extends RuntimeException {

    HttpStatus httpStatus;
    String message;
}

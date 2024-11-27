package com.zyke.minibank.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Getter
@AllArgsConstructor
public class MinibankException extends RuntimeException {

    HttpStatus httpStatus;
    String message;
}

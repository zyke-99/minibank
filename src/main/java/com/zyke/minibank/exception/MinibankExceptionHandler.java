package com.zyke.minibank.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class MinibankExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex) {

        if (ex instanceof MinibankException minibankException) {

            return new ResponseEntity<>(new ErrorResponse(minibankException.getMessage()), minibankException.getHttpStatus());
        } else if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {

            return handleMethodArgumentNotValid(methodArgumentNotValidException);
        } else {

            log.error(ex.getMessage(), ex);
            return new ResponseEntity<>(new ErrorResponse("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {

        String message = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s - %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(". "));

        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }
}

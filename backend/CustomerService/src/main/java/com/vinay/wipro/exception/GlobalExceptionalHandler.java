package com.vinay.wipro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(USER_NOT_FOUND_EXCEPTION.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(USER_NOT_FOUND_EXCEPTION ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "USER_NOT_FOUND_EXCEPTION");
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}

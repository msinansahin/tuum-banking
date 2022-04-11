package com.tuum.banking.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tuum.banking.common.BankingException;

@ControllerAdvice
public class ControllerAdvisor {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidations(MethodArgumentNotValidException ex) {
        var errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(errorMessage, BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BankingException.class)
    public ResponseEntity<String> handleBankingException(BankingException ex) {
        return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
    }

}

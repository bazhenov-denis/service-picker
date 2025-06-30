package com.example.backend.exceptions;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
    Map<String, String> response = new HashMap<>();
    e.getBindingResult()
        .getAllErrors()
        .forEach(
            ex -> {
              String fieldName = ((FieldError) ex).getField();
              String message = ex.getDefaultMessage();
              response.put(fieldName, message);
            }
        );
    return response;
  }
}

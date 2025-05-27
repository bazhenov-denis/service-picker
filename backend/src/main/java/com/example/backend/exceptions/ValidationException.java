package com.example.backend.exceptions;

public class ValidationException extends RuntimeException {

  public ValidationException() {}

  public ValidationException(String message) {
    super(message);
  }
}

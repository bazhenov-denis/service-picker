package com.example.backend.exceptions;

public class ApiException extends RuntimeException {

  public ApiException(String message, Throwable cause) {
    super(message, cause);
  }
}

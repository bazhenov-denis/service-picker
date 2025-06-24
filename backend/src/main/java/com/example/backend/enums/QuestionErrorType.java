package com.example.backend.enums;

public enum QuestionErrorType {
  INVALID_TYPE("Invalid question type"),
  INVALID_REFERENCE_TYPE("Invalid question reference type");

  private final String msg;

  QuestionErrorType(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}

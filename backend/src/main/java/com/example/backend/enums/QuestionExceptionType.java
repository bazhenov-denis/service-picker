package com.example.backend.enums;

public enum QuestionExceptionType {
  INVALID_TYPE("Invalid question type"),
  INVALID_REFERENCE_TYPE("Invalid question reference type"),
  NOT_ENOUGH_OPTIONS("Not enough options provided"),
  WRONG_SCORE_CODE("Score code given doesn't exist"),
  WRONG_ORDER("No order can be made with new positions"),
  DUPLICATED_ID("Several identical ids"),
  WRONG_ID("Id doesn't exist"),
  WRONG_POSITION("Position doesn't exist"),
  UNMODIFIABLE_QUESTION("This question cannot be modified");

  private final String msg;

  QuestionExceptionType(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}

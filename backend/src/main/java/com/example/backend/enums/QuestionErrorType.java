package com.example.backend.enums;

public enum QuestionErrorType {
  INVALID_TYPE("Invalid question type"),
  INVALID_REFERENCE_TYPE("Invalid question reference type"),
  BLANK_QUESTION_TEXT("Question text is blank"),
  BLANK_SHORT_TITLE("Short title is blank"),
  NOT_ENOUGH_OPTIONS("Not enough options provided"),
  BLANK_OPTION_TEXT("Option text is blank"),
  WRONG_NUMBER_OF_SCORES("Wrong quantity of scores provided");

  private final String msg;

  QuestionErrorType(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}

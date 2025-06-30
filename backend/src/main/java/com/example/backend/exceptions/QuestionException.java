package com.example.backend.exceptions;

import com.example.backend.enums.QuestionExceptionType;

public class QuestionException extends RuntimeException {
  private QuestionExceptionType errorType;
  private Long questionId;

  public QuestionExceptionType getErrorType() {
    return errorType;
  }

  public void setErrorType(QuestionExceptionType errorType) {
    this.errorType = errorType;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public QuestionException(QuestionExceptionType errorType, Long questionId) {
    this.errorType = errorType;
    this.questionId = questionId;
  }
}

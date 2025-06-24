package com.example.backend.exceptions;

import com.example.backend.enums.QuestionErrorType;

public class QuestionException extends RuntimeException {
  private QuestionErrorType errorType;
  private Long questionId;

  public QuestionErrorType getErrorType() {
    return errorType;
  }

  public void setErrorType(QuestionErrorType errorType) {
    this.errorType = errorType;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public QuestionException(QuestionErrorType errorType, Long questionId) {
    this.errorType = errorType;
    this.questionId = questionId;
  }
}

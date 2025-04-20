package com.example.backend.DTO;

public class VacancyResult {
  private final boolean success;
  private final String message;
  private final int count;

  public VacancyResult(boolean success, String message, int count) {
    this.success = success;
    this.message = message;
    this.count = count;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public int getCount() {
    return count;
  }
}

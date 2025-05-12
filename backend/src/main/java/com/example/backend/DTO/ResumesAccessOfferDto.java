package com.example.backend.DTO;

public class ResumesAccessOfferDto {

  private Integer civCount;
  private Integer apiLimitedCount;

  public Integer getCivCount() {
    return civCount;
  }

  public void setCivCount(Integer civCount) {
    this.civCount = civCount;
  }

  public Integer getApiLimitedCount() {
    return apiLimitedCount;
  }

  public void setApiLimitedCount(Integer apiLimitedCount) {
    this.apiLimitedCount = apiLimitedCount;
  }
}
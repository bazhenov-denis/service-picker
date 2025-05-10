package com.example.backend.models;

import jakarta.persistence.Entity;

@Entity
public class CombinedOffer {

  private String vacancyType;
  private Integer count;
  private Integer civCount;
  private Integer apiLimitedCount;

  public String getVacancyType() {
    return vacancyType;
  }

  public void setVacancyType(String vacancyType) {
    this.vacancyType = vacancyType;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

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
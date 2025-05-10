package com.example.backend.models;


import jakarta.persistence.Entity;

@Entity
public class VacancyOffer {

  private String vacancyType;
  private Integer count;

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
}

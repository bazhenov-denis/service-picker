package com.example.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "resumes_access_offer")
public class ResumesAccessOffer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer areaId;
  private Integer professionId;
  private Integer accessDuration;
  private Integer numberOfContacts;
  private Double price;

  public Integer getAreaId() {
    return areaId;
  }

  public void setAreaId(Integer areaId) {
    this.areaId = areaId;
  }

  public Integer getProfessionId() {
    return professionId;
  }

  public void setProfessionId(Integer professionId) {
    this.professionId = professionId;
  }

  public Integer getAccessDuration() {
    return accessDuration;
  }

  public void setAccessDuration(Integer accessDuration) {
    this.accessDuration = accessDuration;
  }

  public Integer getNumberOfContacts() {
    return numberOfContacts;
  }

  public void setNumberOfContacts(Integer numberOfContacts) {
    this.numberOfContacts = numberOfContacts;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }
}

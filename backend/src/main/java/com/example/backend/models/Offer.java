package com.example.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Offer {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Integer productId;
  private Integer period;
  private Integer regionId;
  private Integer profroleId;
  private Integer price;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Integer getPeriod() {
    return period;
  }

  public void setPeriod(Integer period) {
    this.period = period;
  }

  public Integer getRegionId() {
    return regionId;
  }

  public void setRegionId(Integer regionId) {
    this.regionId = regionId;
  }

  public Integer getProfroleId() {
    return profroleId;
  }

  public void setProfroleId(Integer profroleId) {
    this.profroleId = profroleId;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }
}

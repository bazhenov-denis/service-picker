package com.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "offers")
public class Offer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "tariff")
  private String tariff;

  @Column(name = "code")
  private String code;

  @Column(name = "child_code_1")
  private String childCode1;

  @Column(name = "child_count_1")
  private Integer childCount1;

  @Column(name = "child_code_2")
  private String childCode2;

  @Column(name = "child_count_2")
  private Integer childCount2;

  @Column(name = "child_code_3")
  private String childCode3;

  @Column(name = "child_count_3")
  private Integer childCount3;

  @Column(name = "child_code_4")
  private String childCode4;

  @Column(name = "child_count_4")
  private Integer childCount4;

  @Column(name = "period")
  private Integer period;

  @Column(name = "region_id")
  private Long regionId;

  @Column(name = "profrole_group_id")
  private Integer profroleGroupId;

  @Column(name = "price_all")
  private Double priceAll;

  @Column(name = "currency")
  private String currency;

  public Offer(
      Long productId,
      String tariff,
      String code,
      String childCode1,
      Integer childCount1,
      String childCode2,
      Integer childCount2,
      String childCode3,
      Integer childCount3,
      String childCode4,
      Integer childCount4,
      Integer period,
      Long regionId,
      Integer profroleGroupId,
      Double priceAll,
      String currency
  ) {
    this.productId = productId;
    this.tariff = tariff;
    this.code = code;
    this.childCode1 = childCode1;
    this.childCount1 = childCount1;
    this.childCode2 = childCode2;
    this.childCount2 = childCount2;
    this.childCode3 = childCode3;
    this.childCount3 = childCount3;
    this.childCode4 = childCode4;
    this.childCount4 = childCount4;
    this.period = period;
    this.regionId = regionId;
    this.profroleGroupId = profroleGroupId;
    this.priceAll = priceAll;
    this.currency = currency;
  }

  public Offer() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getTariff() {
    return tariff;
  }

  public void setTariff(String tariff) {
    this.tariff = tariff;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getChildCode1() {
    return childCode1;
  }

  public void setChildCode1(String childCode1) {
    this.childCode1 = childCode1;
  }

  public Integer getChildCount1() {
    return childCount1;
  }

  public void setChildCount1(Integer childCount1) {
    this.childCount1 = childCount1;
  }

  public String getChildCode2() {
    return childCode2;
  }

  public void setChildCode2(String childCode2) {
    this.childCode2 = childCode2;
  }

  public Integer getChildCount2() {
    return childCount2;
  }

  public void setChildCount2(Integer childCount2) {
    this.childCount2 = childCount2;
  }

  public String getChildCode3() {
    return childCode3;
  }

  public void setChildCode3(String childCode3) {
    this.childCode3 = childCode3;
  }

  public Integer getChildCount3() {
    return childCount3;
  }

  public void setChildCount3(Integer childCount3) {
    this.childCount3 = childCount3;
  }

  public String getChildCode4() {
    return childCode4;
  }

  public void setChildCode4(String childCode4) {
    this.childCode4 = childCode4;
  }

  public Integer getChildCount4() {
    return childCount4;
  }

  public void setChildCount4(Integer childCount4) {
    this.childCount4 = childCount4;
  }

  public Integer getPeriod() {
    return period;
  }

  public void setPeriod(Integer period) {
    this.period = period;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public Integer getProfroleGroupId() {
    return profroleGroupId;
  }

  public void setProfroleGroupId(Integer profroleGroupId) {
    this.profroleGroupId = profroleGroupId;
  }

  public Double getPriceAll() {
    return priceAll;
  }

  public void setPriceAll(Double priceAll) {
    this.priceAll = priceAll;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}

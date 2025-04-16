package com.example.backend.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacancy_offer")
public class VacancyOffer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer areaId;
  private Integer professionId;
  private Integer packageVolume;
  private String vacancyType;
  private Integer publicationPeriod;
  private Double pricePerOne;
  private Double pricePerPackage;

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

  public Integer getPackageVolume() {
    return packageVolume;
  }

  public void setPackageVolume(Integer packageVolume) {
    this.packageVolume = packageVolume;
  }

  public String getVacancyType() {
    return vacancyType;
  }

  public void setVacancyType(String vacancyType) {
    this.vacancyType = vacancyType;
  }

  public Integer getPublicationPeriod() {
    return publicationPeriod;
  }

  public void setPublicationPeriod(Integer publicationPeriod) {
    this.publicationPeriod = publicationPeriod;
  }

  public Double getPricePerOne() {
    return pricePerOne;
  }

  public void setPricePerOne(Double pricePerOne) {
    this.pricePerOne = pricePerOne;
  }

  public Double getPricePerPackage() {
    return pricePerPackage;
  }

  public void setPricePerPackage(Double pricePerPackage) {
    this.pricePerPackage = pricePerPackage;
  }
}

package com.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "region_area_mapping")
public class RegionAreaMapping {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "price_region_id")
  private Long priceRegionId;

  @Column(name = "area_id")
  private Integer areaId;

  public RegionAreaMapping(Long priceRegionId, Integer areaId) {
    this.priceRegionId = priceRegionId;
    this.areaId = areaId;
  }

  public RegionAreaMapping() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPriceRegionId() {
    return priceRegionId;
  }

  public void setPriceRegionId(Long priceRegionId) {
    this.priceRegionId = priceRegionId;
  }

  public Integer getAreaId() {
    return areaId;
  }

  public void setAreaId(Integer areaId) {
    this.areaId = areaId;
  }
}

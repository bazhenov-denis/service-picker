package com.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "profroles_mapping")
public class ProfrolesMapping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "price_profrole_group_id")
  private Integer priceProfroleGroupId;

  @Column(name = "professional_role_id")
  private Integer professionalRoleId;

  public ProfrolesMapping(Integer priceProfroleGroupId, Integer professionalRoleId) {
    this.priceProfroleGroupId = priceProfroleGroupId;
    this.professionalRoleId = professionalRoleId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPriceProfroleGroupId() {
    return priceProfroleGroupId;
  }

  public void setPriceProfroleGroupId(Integer priceProfroleGroupId) {
    this.priceProfroleGroupId = priceProfroleGroupId;
  }

  public Integer getProfessionalRoleId() {
    return professionalRoleId;
  }

  public void setProfessionalRoleId(Integer professionalRoleId) {
    this.professionalRoleId = professionalRoleId;
  }
}

package com.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "price_region")
public class PriceRegion {

  @Id
  private Long id;

  @Column(nullable = false)
  private String name;

  public PriceRegion(Long id, String name, Set<Area> areas) {
    this.id = id;
    this.name = name;
    this.areas = areas;
  }

  @ManyToMany
  @JoinTable(
      name = "area_mapping",
      joinColumns = @JoinColumn(name = "price_region_id"),
      inverseJoinColumns = @JoinColumn(name = "area_id")
  )
  private Set<Area> areas = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Area> getAreas() {
    return areas;
  }

  public void setAreas(Set<Area> areas) {
    this.areas = areas;
  }

  public PriceRegion() {

  }
}

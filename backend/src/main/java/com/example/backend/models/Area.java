package com.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "area")
public class Area {

  @Id
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToMany(mappedBy = "areas")
  private Set<PriceRegion> priceRegions = new HashSet<>();

  public Area(Long id, String name, Set<PriceRegion> priceRegions) {
    this.id = id;
    this.name = name;
    this.priceRegions = priceRegions;
  }

  public Area() {
  }

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

  public Set<PriceRegion> getPriceRegions() {
    return priceRegions;
  }

  public void setPriceRegions(Set<PriceRegion> priceRegions) {
    this.priceRegions = priceRegions;
  }
}

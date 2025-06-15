package com.example.backend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfferListDto {
  private List<OfferDto> items;

  public OfferListDto() {
    this.items = new ArrayList<>();
  }


  public List<OfferDto> getItems() {
    return items;
  }

  public void setItems(List<OfferDto> items) {
    this.items = items;
  }

  @JsonIgnore
  public boolean isEmpty() {
    return items.isEmpty();
  }

  public void add(OfferDto item) {
    items.add(item);
  }

  @JsonIgnore
  public String toString() {
    return this.items.stream().map(OfferDto::toString).collect(Collectors.joining("\n"));
  }
}

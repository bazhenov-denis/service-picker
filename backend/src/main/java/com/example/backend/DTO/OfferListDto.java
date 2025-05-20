package com.example.backend.DTO;

import java.util.List;

public class OfferListDto {
  private List<OfferDto> items;

  public OfferListDto(List<OfferDto> items) {
    this.items = items;
  }

  public List<OfferDto> getItems() {
    return items;
  }

  public void setItems(List<OfferDto> items) {
    this.items = items;
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public void add(OfferDto item) {
    items.add(item);
  }
}

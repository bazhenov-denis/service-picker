package com.example.backend.DTO;

import com.example.backend.exceptions.ValidationException;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimDto {
  private Map<String, List<String>> claimMap = new HashMap<>();

  @JsonAnySetter
  public void addCode(String key, List<String> value) {
    claimMap.put(key, value);
  }

  @JsonAnyGetter
  public Map<String, List<String>> getCodes() {
    return claimMap;
  }

  public Integer getProfessionId() {
    List<String> values = claimMap.get("2");
    if (values.size() != 1) {
      throw new ValidationException("ClaimDto: invalid professionId list size");
    }

    int professionId = Integer.parseInt(values.get(0));
    if (professionId < 1) {
      throw new ValidationException("ClaimDto: invalid professionId");
    }

    return professionId;
  }

  public Integer getAreaId() {
    List<String> values = claimMap.get("1");
    if (values.size() != 1) {
      throw new ValidationException("ClaimDto: invalid areaId list size");
    }

    String[] areas = values.get(0).split("\\.");
    int areaId = Integer.parseInt(areas[1]);
    if (areaId < 1) {
      throw new ValidationException("ClaimDto: invalid areaId");
    }

    return areaId;
  }

  public Integer getQuantity() {
    List<String> values = claimMap.get("3");
    if (values.size() != 1) {
      throw new ValidationException("ClaimDto: invalid quantity list size");
    }

    int quantity = Integer.parseInt(values.get(0));
    if (quantity < 1) {
      throw new ValidationException("ClaimDto: invalid quantity");
    }

    return quantity;
  }

  public Integer getPeriod() {
    List<String> values = claimMap.get("4");
    if (values.size() != 1) {
      throw new ValidationException("ClaimDto: invalid period list size");
    }

    int periodId = Integer.parseInt(values.get(0));
    return switch (periodId) {
      case 1 -> 1;
      case 2 -> 7;
      case 3 -> 30;
      case 4 -> Integer.MAX_VALUE;
      default -> throw new ValidationException("ClaimDto: invalid period");
    };
  }
}


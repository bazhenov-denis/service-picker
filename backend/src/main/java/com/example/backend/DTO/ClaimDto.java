package com.example.backend.DTO;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClaimDto {
  private final Map<String, List<String>> claimMap = new HashMap<>();

  @JsonAnySetter
  public void addCode(String key, List<String> value) {
    claimMap.put(key, value);
  }

  @JsonAnyGetter
  public Map<String, List<String>> getCodes() {
    return claimMap;
  }

  @Override
  public String toString() {
    return claimMap.keySet().stream()
        .map(k -> k + "=" + claimMap.get(k))
        .collect(Collectors.joining(", ", "{", "}"));
  }
}


package com.example.backend.DTO;

import com.example.backend.enums.ScoreCode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class ProcessingResult {
  private final EnumMap<ScoreCode, Long> scores = new EnumMap<>(ScoreCode.class);
  private List<Long> regionIds = Collections.emptyList();
  private List<Long> profRoleIds = Collections.emptyList();

  public long getScore(ScoreCode code) {
    return scores.getOrDefault(code, 0L);
  }

  public void addScore(ScoreCode code, long weight) {
    scores.merge(code, weight, Long::sum);
  }

  public void setRegionIds(List<Long> regionIds) {
    this.regionIds = List.copyOf(regionIds);
  }

  public List<Long> getRegionIds() {
    return regionIds;
  }

  public void setProfRoleIds(List<Long> profRoleIds) {
    this.profRoleIds = List.copyOf(profRoleIds);
  }

  public List<Long> getProfRoleIds() {
    return profRoleIds;
  }

  public Map<ScoreCode, Long> getAllScores() {
    return Map.copyOf(scores);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ProcessingResult{");
    scores.forEach((code, value) -> sb.append(code)
        .append('=')
        .append(value)
        .append(',') );
    sb.append(" regions=").append(regionIds)
        .append(", profRoles=").append(profRoleIds)
        .append('}');
    return sb.toString();
  }
}

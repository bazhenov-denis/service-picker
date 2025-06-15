package com.example.backend.DTO;

import com.example.backend.enums.ScoreCode;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Хранит численные результаты обработки и списки регионов/профессий.
 */
public class ProcessingResult {
  private final EnumMap<ScoreCode, Long> scores = new EnumMap<>(ScoreCode.class);
  private List<Long> regionIds = Collections.emptyList();
  private List<Long> profRoleIds = Collections.emptyList();

  /**
   * Получить накопленную оценку по коду. Если нет — 0.
   */
  public long getScore(ScoreCode code) {
    return scores.getOrDefault(code, 0L);
  }

  /**
   * Прибавить к существующей оценке weight.
   */
  public void addScore(ScoreCode code, long weight) {
    scores.merge(code, weight, Long::sum);
  }

  /**
   * Установить список идентификаторов регионов.
   */
  public void setRegionIds(List<Long> regionIds) {
    this.regionIds = List.copyOf(regionIds);
  }

  /**
   * Получить список идентификаторов регионов.
   */
  public List<Long> getRegionIds() {
    return regionIds;
  }

  /**
   * Установить список идентификаторов профролей.
   */
  public void setProfRoleIds(List<Long> profRoleIds) {
    this.profRoleIds = List.copyOf(profRoleIds);
  }

  /**
   * Получить список идентификаторов профролей.
   */
  public List<Long> getProfRoleIds() {
    return profRoleIds;
  }

  /**
   * Копия всех оценок.
   */
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

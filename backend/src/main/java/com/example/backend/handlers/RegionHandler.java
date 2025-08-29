package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.query.QueryBuilder;
import java.util.List;
import java.util.stream.Collectors; // добавили импорт
import org.springframework.stereotype.Component;

@Component
public class RegionHandler implements CriteriaHandler {
  @Override
  public void apply(ProcessingResult result, QueryBuilder qb) {
    long consistency = result.getScore(ScoreCode.CONSISTENCY);

    List<Long> regionIds = result.getRegionIds();
    if (regionIds != null && !regionIds.isEmpty()) {
      // если consistency меньше 10 — фильтруем по длине строкового представления ID
      if (consistency < 10) {
        regionIds = regionIds.stream()
            .filter(id -> id < 3_000_000L)
            .collect(Collectors.toList());
      }

      // добавляем условие только если после фильтрации что-то осталось
      if (!regionIds.isEmpty()) {
        qb.addCondition(
            "region_id IN (:regionIds)",
            "regionIds",
            regionIds
        );
      }
    }
  }
}

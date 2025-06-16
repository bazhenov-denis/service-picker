package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.query.QueryBuilder;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RegionHandler implements CriteriaHandler {
  @Override
  public void apply(ProcessingResult result, QueryBuilder qb) {
    List<Long> regionIds = result.getRegionIds();
    if (regionIds != null && !regionIds.isEmpty()) {
      qb.addCondition(
          "region_id IN (:regionIds)",
          "regionIds",
          regionIds
      );
    }
  }
}

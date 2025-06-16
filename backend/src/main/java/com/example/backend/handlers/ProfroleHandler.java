package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.query.QueryBuilder;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProfroleHandler implements CriteriaHandler {

  @Override
  public void apply(ProcessingResult result, QueryBuilder qb) {
    List<Long> profRoleIds = result.getProfRoleIds();
    if (profRoleIds != null && !profRoleIds.isEmpty()) {
      qb.addCondition(
          "profrole_group_id IN (:profRoleIds)",
          "profRoleIds",
          profRoleIds
      );
    }
  }
}

package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProfroleHandler implements CriteriaHandler {

  @Override
  public void apply(ProcessingResult result, QueryBuilder qb) {
    List<Long> profRoleIds = new ArrayList<>( result.getProfRoleIds() );
/*    profRoleIds.removeIf(id -> id != null && id == 0L);*/

    if (!profRoleIds.isEmpty()) {
      qb.addCondition(
          "profrole_group_id IN (:profRoleIds)",
          "profRoleIds",
          profRoleIds
      );
    }
  }
}

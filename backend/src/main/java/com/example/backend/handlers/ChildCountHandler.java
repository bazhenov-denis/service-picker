package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.query.QueryBuilder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChildCountHandler implements CriteriaHandler {

  Logger log = LoggerFactory.getLogger(ChildCountHandler.class);

  @Override
  public void apply(ProcessingResult result, QueryBuilder queryBuilder) {

    List<Long> regions = result.getRegionIds();
    List<Long> profroles = result.getProfRoleIds();
    long mass = result.getScore(ScoreCode.MASS);
    long count = regions.size() + profroles.size();
    long exactCount = (count > 15) ? 5L : 1L;
    log.info("Exact child_count filter = {}", exactCount);

    String resumesQuery1, resumesQuery2;

    if (mass < 3) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 100 AND 200) ";
      resumesQuery2 = " OR (child_count_2 BETWEEN 100 AND 200) ";
    } else if (mass < 7) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 200 AND 500) ";
      resumesQuery2 = " OR (child_count_2 BETWEEN 200 AND 500) ";
    } else if (mass < 15) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 500 AND 800) ";
      resumesQuery2 = " OR (child_count_2 BETWEEN 500 AND 800) ";
    } else if (mass < 25) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 1000 AND 1700) ";
      resumesQuery2 = " OR (child_count_2 BETWEEN 700 AND 1000) ";
    } else if (mass < 40) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 1000 AND 1700) ";
      resumesQuery2 = " OR (child_count_2 BETWEEN 1000 AND 1700) ";
    } else {
      resumesQuery1 = " OR (child_count_1 BETWEEN 1600 AND 10000) ";
      resumesQuery2 = " OR (child_count_2 BETWEEN 1600 AND 10000) ";
    }
    String cc1 =
        "(child_count_1 = :exactCount " +
            resumesQuery1 +
            " OR child_count_1 IS NULL)";
    queryBuilder.addCondition(cc1, "exactCount", exactCount);

    log.info("child_count resumes = {}", resumesQuery1);
    log.info("child_count vacancy = {}", exactCount);
  }
}

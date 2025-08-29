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

    long consistency = result.getScore(ScoreCode.CONSISTENCY);
    List<Long> regions = result.getRegionIds();
    List<Long> profroles = result.getProfRoleIds();
    long mass = result.getScore(ScoreCode.MASS);
    long count = regions.size() + profroles.size();
    long exactCount = (mass > 15) ? 5L : 1L;
    log.info("Exact child_count filter = {}", exactCount);

    String resumesQuery1;

    long countContacts = mass + consistency;
    if (countContacts < -10 ) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 100 AND 200) ";
    } else if (countContacts < -5) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 200 AND 600) ";
    } else if (countContacts < 0) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 400 AND 800) ";
    } else if (countContacts < 5) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 600 AND 1000) ";
    } else if (countContacts < 10) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 800 AND 1200) ";
    } else if (countContacts < 15) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 1000 AND 1400) ";
    } else if (countContacts < 20) {
      resumesQuery1 = " OR (child_count_1 BETWEEN 1300 AND 1700) ";
    } else {
      resumesQuery1 = " OR (child_count_1 BETWEEN 2000 AND 10000) ";
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

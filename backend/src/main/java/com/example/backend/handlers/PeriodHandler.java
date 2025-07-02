package com.example.backend.handlers;


import com.example.backend.DTO.ProcessingResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PeriodHandler implements CriteriaHandler{

  Logger logger = LoggerFactory.getLogger(PeriodHandler.class);

  public void apply(ProcessingResult result, QueryBuilder queryBuilder) {
    long consistency = result.getScore(ScoreCode.CONSISTENCY);
    List<Integer> periods = new ArrayList<>();



    if ( consistency < -3) {
      periods.add(7);
      periods.add(14);
      periods.add(30);
    } else if ( consistency < 3) {
      periods.add(14);
      periods.add(30);
      periods.add(92);
    } else if (consistency < 10) {
      periods.add(183);
      periods.add(365);
    } else {
      periods.add(365);
      periods.add(92);
      periods.add(30);
    }

    logger.info("Период{}", periods.toString());



    queryBuilder.addCondition(
        "period IN (:period)",
        "period",
        periods
    );

  }

}

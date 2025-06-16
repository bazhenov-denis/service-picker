package com.example.backend.handlers;


import com.example.backend.DTO.ProcessingResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PeriodHandler implements CriteriaHandler{

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
      periods.add(30);
      periods.add(92);
      periods.add(183);
      periods.add(365);
    } else {
      periods.add(365);
      periods.add(92);
      periods.add(30);
    }

    queryBuilder.addCondition(
        "period IN (:period)",
        "period",
        periods
    );

  }

}

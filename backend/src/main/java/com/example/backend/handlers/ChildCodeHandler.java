package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChildCodeHandler implements CriteriaHandler{

  @Override
  public void apply(ProcessingResult result, QueryBuilder queryBuilder) {
    long urgency = result.getScore(ScoreCode.URGENCY);
    long competition = result.getScore(ScoreCode.COMPETITION);

    List<String> codes = new ArrayList<>();

    codes.add("CIV");
    codes.add("API_LIMITED");

    if (competition < 30 && urgency < 20) {
      codes.add("VP");
    }
    else if (competition < 100 && urgency < 10) {
      codes.add("RENEWAL_VP");
    }
    else  {
      codes.add("VPREM");
    }

    queryBuilder.addCondition(
        "(child_code_1 IN (:childCodes1) OR child_code_1 IS NULL)",
        "childCodes1",
        codes
    );

    queryBuilder.addCondition(
        "(child_code_2 IN (:childCodes2) OR child_code_2 IS NULL)",
        "childCodes2",
        codes
    );

  }
}

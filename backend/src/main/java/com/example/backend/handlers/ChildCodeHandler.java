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
public class ChildCodeHandler implements CriteriaHandler{

  Logger log = LoggerFactory.getLogger(ChildCodeHandler.class);

  @Override
  public void apply(ProcessingResult result, QueryBuilder queryBuilder) {
    long urgency = result.getScore(ScoreCode.URGENCY);
    long competition = result.getScore(ScoreCode.COMPETITION);
    long mass = result.getScore(ScoreCode.MASS);

    List<String> codes = new ArrayList<>();

    codes.add("CIV");
    codes.add("API_LIMITED");

    if ( urgency > 8 || competition > 4250) {
      codes.add("VPREM");
    }
    else if (competition > 30 || urgency > 5) {
      codes.add("RENEWAL_VP");
    }
    else  {
      codes.add("VP");
    }

    if ( mass > 40) {
      codes.add("VP");
    }


    // один и тот же плейсхолдер :codes
    queryBuilder.addCondition(
        "(child_code_1 IN (:childCode1) OR child_code_1 IS NULL OR child_code_1 = '')",
        "childCode1",
        codes
    );
    queryBuilder.addCondition(
        "(child_code_2 IN (:childCode2) OR child_code_2 IS NULL OR child_code_2 = '')",
        "childCode2",
        codes
    );

    log.info("Child Code : {}", codes);

  }
}

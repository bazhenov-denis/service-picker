package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class CodeHandler implements CriteriaHandler {

  Logger logger = Logger.getLogger(CodeHandler.class.getName());

  public void apply(ProcessingResult result, QueryBuilder queryBuilder) {

    long competition = result.getScore(ScoreCode.COMPETITION);
    long resumeScore = result.getScore(ScoreCode.ACCESS_RESUMES);
    long mass = result.getScore(ScoreCode.MASS);

    List<String> codes = new ArrayList<>();

    if (resumeScore > 3 && mass > 40) {
      codes.add("CIV+VPPL");
    }
    else if (resumeScore > 5) {
      codes.add("DI");
    }
    else if (competition <= 100) {
      codes.add("VPPL");
    }

    if (codes.isEmpty()) {
      codes.add("VPPL");
    }

    logger.info("Тип услуги:" + codes);


    queryBuilder.addCondition(
        "code IN (:codes)",
        "codes",
        codes
    );
  }
}

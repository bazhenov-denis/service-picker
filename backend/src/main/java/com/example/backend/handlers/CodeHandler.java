package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CodeHandler implements CriteriaHandler {


  public void apply(ProcessingResult result, QueryBuilder queryBuilder) {

    long vacancyScore = result.getScore(ScoreCode.ACCESS_VACANCIES);
    long resumeScore = result.getScore(ScoreCode.ACCESS_RESUMES);
    long mass = result.getScore(ScoreCode.MASS);

    List<String> codes = new ArrayList<>();


    if (vacancyScore > 5) {
      codes.add("VPPL");
    }
    if (resumeScore > 5) {
      codes.add("DI");
    }
    if (vacancyScore > 3 && resumeScore > 3 && mass > 3) {
      codes.add("CIV+VPPL");

    }
    if (codes.isEmpty()) {
      codes.add("VPPL");
    }

    queryBuilder.addCondition(
        "code IN (:codes)",
        "codes",
        codes
    );
  }
}

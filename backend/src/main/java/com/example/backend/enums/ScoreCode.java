package com.example.backend.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ScoreCode {
  ACCESS_VACANCIES("access_vacancies"),
  ACCESS_RESUMES("access_resumes"),
  COMPETITION("competition"),
  URGENCY("urgency"),
  CONSISTENCY("consistency"),
  MASS("mass"),
  REGION("region"),
  PROFESSION("profession");

  private final String code;

  ScoreCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  private static final Map<String, ScoreCode> BY_CODE =
      Arrays.stream(values()).collect(Collectors.toMap(ScoreCode::getCode, e -> e));

  public static ScoreCode fromCode(String code) {
    return BY_CODE.get(code);
  }
}

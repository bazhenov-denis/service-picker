package com.example.backend.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum QuestionType {
  REFERENCE("reference"),
  INPUT("input"),
  SINGLE_CHOICE("single-choice"),
  MULTIPLE_CHOICE("multiple-choice");

  private final String type;

  QuestionType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  private static final Map<String, QuestionType> FROM_STR =
      Arrays.stream(values()).collect(Collectors.toMap(QuestionType::getType, e -> e));

  public static QuestionType fromStr(String type) {
    return FROM_STR.get(type);
  }

  public static boolean contains(String type) {
    return FROM_STR.containsKey(type);
  }
}

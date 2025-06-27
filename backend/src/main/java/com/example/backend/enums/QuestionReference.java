package com.example.backend.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum QuestionReference {
  PROFESSIONS("professions"),
  REGIONS("regions");

  private final String str;

  QuestionReference(String str) {
    this.str = str;
  }

  public String getStr() {
    return str;
  }

  private static final Map<String, QuestionReference> FROM_STR =
      Arrays.stream(values()).collect(Collectors.toMap(QuestionReference::getStr, e -> e));

  public static QuestionReference fromStr(String str) {
    return FROM_STR.get(str);
  }

  public static boolean contains(String str) {
    return FROM_STR.containsKey(str);
  }
}

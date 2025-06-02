package com.example.backend.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum OfferChildCodeEnum {
  AP("AP", "Анонимная вакансия"),
  CIV("CIV", "Поиск по базе резюме"),
  RENEWAL_VP("RENEWAL_VP", "Стандарт Плюс"),
  VP("VP", "Стандарт"),
  VPREM("VPREM", "Премиум");

  private final String code;
  private final String label;
  private static final Map<String, OfferChildCodeEnum> CODE_MAP;

  static {
    Map<String, OfferChildCodeEnum> tmp = new HashMap<>();
    for (OfferChildCodeEnum e : values()) {
      tmp.put(e.code, e);
    }
    CODE_MAP = Collections.unmodifiableMap(tmp);
  }

  OfferChildCodeEnum(String code, String label) {
    this.code = code;
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static OfferChildCodeEnum fromCodeOrDefault(String code) {
    if (code == null) {
      return VP;
    }
    OfferChildCodeEnum e = CODE_MAP.get(code);
    return (e != null ? e : VP);
  }

  public static String getLabelByCode(String code) {
    return fromCodeOrDefault(code).getLabel();
  }
}

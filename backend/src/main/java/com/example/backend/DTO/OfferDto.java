package com.example.backend.DTO;

public record OfferDto(
    String label,
    String type,
    String title,
    String period,
    String region,
    String profroleGroup,
    String price,
    String vacancyType,
    String vacancyCount,
    String civCount,
    String apiLimitedCount
) {
  @Override
  public String toString() {
    return String.format(
        "{ label=%s, type=%s, title=%s, period=%s, region=%s, profroleGroup=%s, " +
            "price=%s, vacancyType=%s, vacancyCount=%s, civCount=%s, apiLimitedCount=%s }",
        label,
        type,
        title,
        period,
        region,
        profroleGroup,
        price,
        vacancyType,
        vacancyCount,
        civCount,
        apiLimitedCount
    );
  }
}

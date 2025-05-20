package com.example.backend.DTO;

public record OfferDto(
    String label, String type, String title, String period, String region, String profroleGroup, String price,
    String vacancyType, String vacancyCount, String civCount, String apiLimitedCount) {
}

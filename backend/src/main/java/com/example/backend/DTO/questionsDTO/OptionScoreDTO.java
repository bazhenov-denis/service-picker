package com.example.backend.DTO.questionsDTO;

public record OptionScoreDTO(
    Long id,
    String code,
    String title,
    Long weight
) {
}
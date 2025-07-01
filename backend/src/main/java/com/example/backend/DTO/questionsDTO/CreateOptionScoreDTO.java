package com.example.backend.DTO.questionsDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOptionScoreDTO(
    @NotBlank String code,
    @NotNull Long weight
) {
}
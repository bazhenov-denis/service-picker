package com.example.backend.DTO.questionsDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ModifiableQuestionDTO(
    @NotNull @Min(1L) Long id,
    @NotNull @Min(1) Integer position,
    @NotNull Boolean isActive
) {
}

package com.example.backend.DTO.questionsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CreateQuestionDTO(
    @NotBlank String questionText,
    @NotNull String type,
    @NotNull Boolean isRequired,
    String referenceType,
    @NotBlank String shortTitle,
    @NotNull Boolean active,
    @Valid List<CreateOptionDTO> options
) {}
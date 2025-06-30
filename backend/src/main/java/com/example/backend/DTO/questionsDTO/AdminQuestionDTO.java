package com.example.backend.DTO.questionsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AdminQuestionDTO(
    Long id,
    String questionText,
    String type,
    Boolean isRequired,
    String referenceType,
    String shortTitle,
    Integer position,
    Boolean active,
    List<OptionDTO> options
) {}
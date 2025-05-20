package com.example.backend.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record QuestionDTO(
    Long id,
    String questionText,
    String type,
    Boolean isRequired,
    String referenceType,
    List<OptionDTO> options
) {
}
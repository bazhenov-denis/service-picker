package com.example.backend.DTO.questionsDTO;

import jakarta.validation.Valid;
import java.util.List;

public record UpdateQuestionsDTO(@Valid List<ModifiableQuestionDTO> modifiableQuestionDTOList) {
}

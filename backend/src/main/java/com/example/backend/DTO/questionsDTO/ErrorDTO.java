package com.example.backend.DTO.questionsDTO;

import com.example.backend.enums.QuestionErrorType;

public record ErrorDTO(QuestionErrorType errorType, Long questionId, String message) {
}

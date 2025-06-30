package com.example.backend.DTO.questionsDTO;

import com.example.backend.enums.QuestionExceptionType;

public record QuestionExceptionDTO(QuestionExceptionType exceptionType, Long questionId, String message) {
}

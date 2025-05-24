package com.example.backend.controllers;

import com.example.backend.DTO.QuestionDTO;
import com.example.backend.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "Questions",
    description = "Операции для работы с вопросами"
)

@RestController
@RequestMapping("/questions")
public class QuestionController {
  private final QuestionService questionService;
  public QuestionController(QuestionService questionService) { this.questionService = questionService; }

  @Operation(
      summary = "Получить список всех вопросов",
      description = "Возвращает массив DTO с полным набором вопросов"
  )
  @GetMapping
  public List<QuestionDTO> list() {
    return questionService.getAllQuestions();
  }
}

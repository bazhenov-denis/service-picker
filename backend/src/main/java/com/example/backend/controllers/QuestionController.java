package com.example.backend.controllers;

import com.example.backend.DTO.questionsDTO.AdminQuestionDTO;
import com.example.backend.DTO.questionsDTO.QuestionDTO;
import com.example.backend.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      summary = "Получить список вопросов",
      description = "role = client (по умолчанию) — только активные вопросы без оценок; " +
          "role = admin — все вопросы с полем active и оценками"
  )
  @GetMapping
  public ResponseEntity<?> list(
      @RequestParam(name = "role", defaultValue = "client") String role
  ) {
    if ("admin".equalsIgnoreCase(role)) {
      List<AdminQuestionDTO> all = questionService.getAdminQuestions();
      return ResponseEntity.ok(all);
    } else {
      List<QuestionDTO> active = questionService.getClientQuestions();
      return ResponseEntity.ok(active);
    }
  }
}

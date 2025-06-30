package com.example.backend.controllers;

import com.example.backend.DTO.questionsDTO.AdminQuestionDTO;
import com.example.backend.DTO.questionsDTO.CreateQuestionDTO;
import com.example.backend.DTO.questionsDTO.QuestionExceptionDTO;
import com.example.backend.DTO.questionsDTO.QuestionDTO;
import com.example.backend.exceptions.QuestionException;
import com.example.backend.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
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

  @Operation(
      summary = "Create new question",
      description = "Create new question with new options with scores"
  )
  @PostMapping
  public ResponseEntity<?> createQuestion(@Valid @RequestBody CreateQuestionDTO createQuestionDTO) {
    try {
      AdminQuestionDTO body = questionService.createQuestion(createQuestionDTO);
      return ResponseEntity.ok(body);
    } catch (QuestionException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new QuestionExceptionDTO(e.getErrorType(), e.getQuestionId(), e.getErrorType().getMsg()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, something gone wrong");
    }
  }
}

package com.example.backend.controllers;

import com.example.backend.DTO.QuestionDTO;
import com.example.backend.services.QuestionService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
public class QuestionController {
  private final QuestionService questionService;
  public QuestionController(QuestionService questionService) { this.questionService = questionService; }

  @GetMapping
  public List<QuestionDTO> list() {
    return questionService.getAllQuestions();
  }
}

package com.example.backend.controllers;

import com.example.backend.DTO.ScoreTypeDTO;
import com.example.backend.services.ScoreTypeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/score-types")
public class ScoreTypeController {

  private final ScoreTypeService service;

  public ScoreTypeController(ScoreTypeService service) {
    this.service = service;
  }

  @Operation(
      summary = "Получить все типы оценок",
      description = "Возвращает список ScoreTypeDTO с полями id, code и title"
  )
  @GetMapping
  public List<ScoreTypeDTO> list() {
    return service.getAllScoreTypes();
  }
}

package com.example.backend.DTO.questionsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOptionDTO {
  @NotBlank
  private String text;

  @NotEmpty
  private List<Long> scores;

  public CreateOptionDTO() {}

  public CreateOptionDTO(String text, List<Long> scores) {
    this.text = text;
    this.scores = scores;
  }

  public String getText() {
    return text;
  }

  public List<Long> getScores() {
    return scores;
  }
}

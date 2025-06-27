package com.example.backend.DTO.questionsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOptionDTO {
  private String text;
  private List<Integer> scores;

  public CreateOptionDTO() {}

  public CreateOptionDTO(String text) {
    this.text = text;
  }

  public CreateOptionDTO(String text, List<Integer> scores) {
    this(text);
    this.scores = scores;
  }

  public String getText() {
    return text;
  }

  public List<Integer> getScores() {
    return scores;
  }
}

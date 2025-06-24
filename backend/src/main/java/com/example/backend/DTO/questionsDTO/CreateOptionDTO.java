package com.example.backend.DTO.questionsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOptionDTO {
  private String text;
  private Integer position;
  private List<OptionScoreDTO> scores;

  public CreateOptionDTO() {}

  public CreateOptionDTO(String text, Integer position) {
    this.text = text;
    this.position = position;
  }

  public CreateOptionDTO(String text, Integer position, List<OptionScoreDTO> scores) {
    this(text, position);
    this.scores = scores;
  }

  public String getText() {
    return text;
  }

  public Integer getPosition() {
    return position;
  }

  public List<OptionScoreDTO> getScores() {
    return scores;
  }
}

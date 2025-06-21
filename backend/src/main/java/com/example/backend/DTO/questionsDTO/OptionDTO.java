package com.example.backend.DTO.questionsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OptionDTO {
  private Long id;
  private String text;
  private Integer position;
  private List<OptionScoreDTO> scores;

  public OptionDTO() {}

  public OptionDTO(Long id, String text, Integer position) {
    this.id = id;
    this.text = text;
    this.position = position;
  }

  public OptionDTO(Long id, String text, Integer position, List<OptionScoreDTO> scores) {
    this(id, text, position);
    this.scores = scores;
  }

  public Long getId() {
    return id;
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

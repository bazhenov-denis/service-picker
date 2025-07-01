package com.example.backend.DTO.questionsDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CreateOptionDTO {
  @NotBlank
  private String text;

  @Valid
  @NotNull
  private CreateOptionScoreDTO score;

  public CreateOptionDTO() {}

  public CreateOptionDTO(String text, CreateOptionScoreDTO score) {
    this.text = text;
    this.score = score;
  }

  public String getText() {
    return this.text;
  }

  public CreateOptionScoreDTO getScore() {
    return this.score;
  }
}

package com.example.backend.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionDTO {
  private Long id;
  private String questionText;
  private String type;
  private Boolean isRequired;
  private String referenceType;
  private List<OptionDTO> options;

  public QuestionDTO() {
  }

  public QuestionDTO(
      Long id,
      String questionText,
      String type,
      Boolean isRequired,
      String referenceType,
      List<OptionDTO> options
  ) {
    this.id = id;
    this.questionText = questionText;
    this.type = type;
    this.isRequired = isRequired;
    this.referenceType = referenceType;
    this.options = options;
  }

  public Long getId() {
    return id;
  }

  public String getQuestionText() {
    return questionText;
  }

  public String getType() {
    return type;
  }

  public Boolean getIsRequired() {
    return isRequired;
  }

  public String getReferenceType() {
    return referenceType;
  }

  public List<OptionDTO> getOptions() {
    return options;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setIsRequired(Boolean isRequired) {
    this.isRequired = isRequired;
  }

  public void setReferenceType(String referenceType) {
    this.referenceType = referenceType;
  }

  public void setOptions(List<OptionDTO> options) {
    this.options = options;
  }
}

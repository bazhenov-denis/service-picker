package com.example.backend.models;

import com.example.backend.DTO.questionsDTO.AdminQuestionDTO;
import com.example.backend.DTO.questionsDTO.OptionDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "questions")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "question_text", nullable = false)
  private String questionText;

  @Column(name = "type", nullable = false)
  private String type;

  @Column(name = "is_required")
  private Boolean isRequired;

  @Column(name = "reference_type")
  private String referenceType;

  @Column(name = "short_title")
  private String shortTitle;

  @Column(name = "position")
  private Integer position;

  @Column(name = "active", nullable = false)
  private Boolean active = true;

  @OneToMany(
      mappedBy = "question",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<Option> options;

  public Question() {

  }

  public Question(
      Long id,
      String questionText,
      String type,
      Boolean isRequired,
      String referenceType,
      String shortTitle,
      Integer position,
      List<Option> options
  ) {
    this.id = id;
    this.questionText = questionText;
    this.type = type;
    this.isRequired = isRequired;
    this.referenceType = referenceType;
    this.shortTitle = shortTitle;
    this.position = position;
    this.options = options;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Boolean getIsRequired() {
    return isRequired;
  }

  public void setRequired(Boolean required) {
    isRequired = required;
  }

  public String getReferenceType() {
    return referenceType;
  }

  public void setReferenceType(String referenceType) {
    this.referenceType = referenceType;
  }

  public List<Option> getOptions() {
    return options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }

  public Boolean getRequired() {
    return isRequired;
  }

  public String getShortTitle() {
    return shortTitle;
  }

  public void setShortTitle(String shortTitle) {
    this.shortTitle = shortTitle;
  }


  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public AdminQuestionDTO toDto() {
    List<OptionDTO> optionDTOList = this.options != null ? this.options.stream().map(Option::toDto).toList() : null;

    return new AdminQuestionDTO(
        this.id,
        this.questionText,
        this.type,
        this.isRequired,
        this.referenceType,
        this.shortTitle,
        this.position,
        this.active,
        optionDTOList
    );
  }
}

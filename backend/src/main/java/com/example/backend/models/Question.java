package com.example.backend.models;

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

@Entity
@Table(name = "questions")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
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

  @Column(name = "code", length = 100)
  private String code;

  @Column(name = "position")
  private Integer position;

  @OneToMany(
      mappedBy = "question",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<Option> options;

  public Question() {

  }


  public Question(Long id,
      String questionText,
      String type,
      Boolean isRequired,
      String referenceType,
      String shortTitle,
      String code,
      Integer position,
      List<Option> options) {
    this.id = id;
    this.questionText = questionText;
    this.type = type;
    this.isRequired = isRequired;
    this.referenceType = referenceType;
    this.shortTitle = shortTitle;
    this.code = code;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

}

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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "score_types")
public class ScoreType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "title")
  private String title;

  public ScoreType(Long id, String code, String title, List<OptionScore> optionScores) {
    this.id = id;
    this.code = code;
    this.title = title;
    this.optionScores = optionScores;
  }

  @OneToMany(
      mappedBy = "scoreType",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<OptionScore> optionScores = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<OptionScore> getOptionScores() {
    return optionScores;
  }

  public void setOptionScores(List<OptionScore> optionScores) {
    this.optionScores = optionScores;
  }

  public ScoreType() {

  }
}


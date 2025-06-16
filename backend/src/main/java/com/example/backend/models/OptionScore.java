package com.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "option_scores")
public class OptionScore {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "option_id", nullable = false)
  private Option option;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "score_type_id", nullable = false)
  private ScoreType scoreType;

  @Column(name = "weight", nullable = false)
  private Long weight;

  public OptionScore(Long id, Option option, ScoreType scoreType, Long weight) {
    this.id = id;
    this.option = option;
    this.scoreType = scoreType;
    this.weight = weight;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Option getOption() {
    return option;
  }

  public void setOption(Option option) {
    this.option = option;
  }

  public ScoreType getScoreType() {
    return scoreType;
  }

  public void setScoreType(ScoreType scoreType) {
    this.scoreType = scoreType;
  }

  public Long getWeight() {
    return weight;
  }

  public void setWeight(Long weight) {
    this.weight = weight;
  }

  public OptionScore() {

  }
}

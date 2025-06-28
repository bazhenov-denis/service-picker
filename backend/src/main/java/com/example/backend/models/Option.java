package com.example.backend.models;

import com.example.backend.DTO.questionsDTO.OptionDTO;
import com.example.backend.DTO.questionsDTO.OptionScoreDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "options")
public class Option {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String text;

  @ManyToOne
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  @Column(name = "position")
  private Integer position;

  @OneToMany(
      mappedBy = "option",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private List<OptionScore> optionScores = new ArrayList<>();

  public Option() {
  }

  public Option(Long id, String text, Question question, Integer position) {
    this.id = id;
    this.text = text;
    this.question = question;
    this.position = position;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public List<OptionScore> getOptionScores() {
    return optionScores;
  }

  public void setOptionScores(List<OptionScore> optionScores) {
    this.optionScores = optionScores;
  }

  public OptionDTO toDto() {
    List<OptionScoreDTO> optionScoreDTOList = this.optionScores.stream().map(OptionScore::toDto).toList();

    return new OptionDTO(
        this.id,
        this.text,
        this.position,
        optionScoreDTOList
    );
  }
}

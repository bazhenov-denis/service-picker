package com.example.backend.DTO;

public class OptionDTO {
  private Long id;
  private String text;

  public OptionDTO() {
  }

  public OptionDTO(Long id, String text) {
    this.id = id;
    this.text = text;
  }

  public Long getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setText(String text) {
    this.text = text;
  }
}

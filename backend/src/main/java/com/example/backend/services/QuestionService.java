package com.example.backend.services;

import com.example.backend.DAO.QuestionDao;
import com.example.backend.DTO.OptionDTO;
import com.example.backend.DTO.QuestionDTO;
import com.example.backend.models.Question;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {
  private final QuestionDao dao;

  public QuestionService(QuestionDao dao) {
    this.dao = dao;
  }

  @Transactional(readOnly = true)
  public List<QuestionDTO> getAllQuestions() {
    return dao.findAllWithOptions().stream()
        .map(this::mapToDto)
        .toList();
  }

  private QuestionDTO mapToDto(Question q) {
    List<OptionDTO> optionDto = q.getOptions().stream()
        .map(o -> new OptionDTO(o.getId(), o.getText()))
        .toList();

    return new QuestionDTO(
        q.getId(),
        q.getQuestionText(),
        q.getType(),
        q.getIsRequired(),
        q.getReferenceType(),
        optionDto
    );
  }
}

package com.example.backend.services;

import com.example.backend.DAO.QuestionDao;
import com.example.backend.DTO.questionsDTO.AdminQuestionDTO;
import com.example.backend.DTO.questionsDTO.CreateQuestionDTO;
import com.example.backend.DTO.questionsDTO.OptionDTO;
import com.example.backend.DTO.questionsDTO.OptionScoreDTO;
import com.example.backend.DTO.questionsDTO.QuestionDTO;
import com.example.backend.models.Option;
import com.example.backend.models.OptionScore;
import com.example.backend.models.Question;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QuestionService {
  private final QuestionDao dao;
  public QuestionService(QuestionDao dao) { this.dao = dao; }

  public List<QuestionDTO> getClientQuestions() {
    return dao.findActiveWithOptions().stream()
        .map(this::mapToClientDto)
        .toList();
  }

  public List<AdminQuestionDTO> getAdminQuestions() {
    List<Question> questions = dao.findAllWithOptions();
    List<Long> allOptionIds = questions.stream()
        .flatMap(q -> q.getOptions().stream().map(Option::getId))
        .toList();
    List<OptionScore> scores = dao.findScoresByOptionIds(allOptionIds);
    Map<Long, List<OptionScoreDTO>> scoresByOption = scores.stream()
        .map(os -> new OptionScoreDTO(
            os.getId(),
            os.getScoreType().getCode(),
            os.getScoreType().getTitle(),
            os.getWeight()
        ))
        .collect(Collectors.groupingBy(OptionScoreDTO::id));

    return questions.stream()
        .map(q -> mapToAdminDto(q, scoresByOption))
        .toList();
  }

  public AdminQuestionDTO createQuestion(CreateQuestionDTO createQuestionDTO) {

  }

  private QuestionDTO mapToClientDto(Question q) {
    List<OptionDTO> opts = q.getOptions().stream()
        .map(o -> new OptionDTO(o.getId(), o.getText(), o.getPosition()))
        .toList();
    return new QuestionDTO(
        q.getId(),
        q.getQuestionText(),
        q.getType(),
        q.getIsRequired(),
        q.getReferenceType(),
        q.getShortTitle(),
        q.getPosition(),
        opts
    );
  }

  private AdminQuestionDTO mapToAdminDto(Question q, Map<Long,List<OptionScoreDTO>> scoresByOption) {
    List<OptionDTO> opts = q.getOptions().stream()
        .map(o -> new OptionDTO(
            o.getId(),
            o.getText(),
            o.getPosition(),
            scoresByOption.getOrDefault(o.getId(), List.of())
        ))
        .toList();
    return new AdminQuestionDTO(
        q.getId(),
        q.getQuestionText(),
        q.getType(),
        q.getIsRequired(),
        q.getReferenceType(),
        q.getShortTitle(),
        q.getPosition(),
        q.getActive(),
        opts
    );
  }
}

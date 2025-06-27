package com.example.backend.services;

import com.example.backend.DAO.QuestionDao;
import com.example.backend.DTO.questionsDTO.AdminQuestionDTO;
import com.example.backend.DTO.questionsDTO.CreateOptionDTO;
import com.example.backend.DTO.questionsDTO.CreateQuestionDTO;
import com.example.backend.DTO.questionsDTO.OptionDTO;
import com.example.backend.DTO.questionsDTO.OptionScoreDTO;
import com.example.backend.DTO.questionsDTO.QuestionDTO;
import com.example.backend.enums.QuestionErrorType;
import com.example.backend.enums.QuestionReference;
import com.example.backend.enums.QuestionType;
import com.example.backend.enums.ScoreCode;
import com.example.backend.exceptions.QuestionException;
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
    if (createQuestionDTO.questionText().isBlank()) {
      throw new QuestionException(QuestionErrorType.BLANK_QUESTION_TEXT, null);
    }

    if (createQuestionDTO.shortTitle().isBlank()) {
      throw new QuestionException(QuestionErrorType.BLANK_SHORT_TITLE, null);
    }

    if (!QuestionType.contains(createQuestionDTO.type())) {
      throw new QuestionException(QuestionErrorType.INVALID_TYPE, null);
    }

    if (createQuestionDTO.type().equals(QuestionType.REFERENCE.getType())) {
      if (!QuestionReference.contains(createQuestionDTO.referenceType())) {
        throw new QuestionException(QuestionErrorType.INVALID_REFERENCE_TYPE, null);
      }
    } else if (createQuestionDTO.referenceType() != null) {
      throw new QuestionException(QuestionErrorType.INVALID_REFERENCE_TYPE, null);
    }

    if (createQuestionDTO.type().equals(QuestionType.SINGLE_CHOICE.getType())
        || createQuestionDTO.type().equals(QuestionType.MULTIPLE_CHOICE.getType())) {
      if (createQuestionDTO.options().size() < 2) {
        throw new QuestionException(QuestionErrorType.NOT_ENOUGH_OPTIONS, null);
      }
    }

    for (CreateOptionDTO createOptionDTO : createQuestionDTO.options()) {
      if (createOptionDTO.getText().isBlank()) {
        throw new QuestionException(QuestionErrorType.BLANK_OPTION_TEXT, null);
      }

      if (createOptionDTO.getScores().size() != ScoreCode.values().length) {
        throw new QuestionException(QuestionErrorType.NOT_ENOUGH_SCORES, null);
      }
    }


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

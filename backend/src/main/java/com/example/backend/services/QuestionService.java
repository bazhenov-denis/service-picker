package com.example.backend.services;

import com.example.backend.DAO.OptionDao;
import com.example.backend.DAO.OptionScoreDao;
import com.example.backend.DAO.QuestionDao;
import com.example.backend.DAO.ScoreTypeDao;
import com.example.backend.DTO.questionsDTO.AdminQuestionDTO;
import com.example.backend.DTO.questionsDTO.CreateOptionDTO;
import com.example.backend.DTO.questionsDTO.CreateQuestionDTO;
import com.example.backend.DTO.questionsDTO.ErrorDTO;
import com.example.backend.DTO.questionsDTO.ErrorListDTO;
import com.example.backend.DTO.questionsDTO.ModifiableQuestionDTO;
import com.example.backend.DTO.questionsDTO.OptionDTO;
import com.example.backend.DTO.questionsDTO.OptionScoreDTO;
import com.example.backend.DTO.questionsDTO.QuestionDTO;
import com.example.backend.DTO.questionsDTO.UpdateQuestionsDTO;
import com.example.backend.enums.QuestionErrorType;
import com.example.backend.enums.QuestionReference;
import com.example.backend.enums.QuestionType;
import com.example.backend.enums.ScoreCode;
import com.example.backend.exceptions.QuestionException;
import com.example.backend.models.Option;
import com.example.backend.models.OptionScore;
import com.example.backend.models.Question;
import com.example.backend.models.ScoreType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class QuestionService {
  private final QuestionDao dao;

  @Autowired
  private ScoreTypeDao scoreTypeDao;

  @Autowired
  private OptionDao optionDao;

  @Autowired
  private OptionScoreDao optionScoreDao;

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

  @Transactional
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
        throw new QuestionException(QuestionErrorType.WRONG_NUMBER_OF_SCORES, null);
      }
    }

    Question question = new Question();
    question.setQuestionText(createQuestionDTO.questionText());
    question.setActive(createQuestionDTO.active());
    question.setPosition(dao.getNextPosition());
    question.setType(createQuestionDTO.type());
    question.setReferenceType(createQuestionDTO.referenceType());
    question.setShortTitle(createQuestionDTO.shortTitle());
    question.setRequired(createQuestionDTO.isRequired());

    if (createQuestionDTO.type().equals(QuestionType.SINGLE_CHOICE.getType())
        || createQuestionDTO.type().equals(QuestionType.MULTIPLE_CHOICE.getType())) {
      List<Option> options = new ArrayList<>();
      for (int i = 0; i < createQuestionDTO.options().size(); i++) {
        List<ScoreType> scoreTypes = scoreTypeDao.findAll();
        Option option = new Option();
        List<OptionScore> scores = new ArrayList<>();
        for (int j = 0; j < scoreTypes.size(); j++) {
          OptionScore optionScore = new OptionScore();
          optionScore.setScoreType(scoreTypes.get(j));
          optionScore.setWeight(createQuestionDTO.options().get(i).getScores().get(j));
          optionScore.setOption(option);
          scores.add(optionScore);
        }

        option.setPosition(i + 1);
        option.setText(createQuestionDTO.options().get(i).getText());
        option.setOptionScores(scores);
        option.setQuestion(question);

        options.add(option);
      }

      question.setOptions(options);
    }

    dao.save(question);
    for (Option option : question.getOptions()) {
      optionDao.save(option);
      for (OptionScore optionScore : option.getOptionScores()) {
        optionScoreDao.save(optionScore);
      }
    }

    return question.toDto();
  }

  @Transactional
  public ErrorListDTO updateQuestions(UpdateQuestionsDTO updateQuestionsDTO) {
    List<ErrorDTO> errors = new ArrayList<>();

    // null fields in request dto are not allowed
    for (ModifiableQuestionDTO item : updateQuestionsDTO.modifiableQuestionDTOList()) {
      if (item.id() == null || item.position() == null || item.isActive() == null) {
        errors.add(new ErrorDTO(QuestionErrorType.NULL_FIELD, item.id(), QuestionErrorType.NULL_FIELD.getMsg()));
      }
    }

    // null fields are destructive for further checks
    if (!errors.isEmpty()) {
      return new ErrorListDTO(errors);
    }

    // source and destination position sets must match
    Set<Long> ids = updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .map(ModifiableQuestionDTO::id)
        .collect(Collectors.toSet());
    Set<Integer> oldPositions = dao.getPositionsByIds(ids);
    Set<Integer> newPositions = updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .map(ModifiableQuestionDTO::position)
        .collect(Collectors.toSet());
    if (!oldPositions.equals(newPositions)) {
      errors.add(new ErrorDTO(QuestionErrorType.WRONG_ORDER, null, QuestionErrorType.WRONG_ORDER.getMsg()));
    }

    // question id must exist
    long maxId = dao.getMaxId();
    List<Long> wrongIds = updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .map(ModifiableQuestionDTO::id)
        .filter(e -> e < 1 || e > maxId)
        .toList();
    for (Long wrongId : wrongIds) {
      errors.add(new ErrorDTO(QuestionErrorType.WRONG_ID, wrongId, QuestionErrorType.WRONG_ID.getMsg()));
    }

    // question position must fit order
    int maxPosition = dao.getNextPosition() - 1;
    List<ModifiableQuestionDTO> wrongPositionedItems = updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .filter(e -> e.position() < 1 || e.position() > maxPosition)
        .toList();
    for (ModifiableQuestionDTO q : wrongPositionedItems) {
      errors.add(new ErrorDTO(QuestionErrorType.WRONG_POSITION, q.id(), QuestionErrorType.WRONG_POSITION.getMsg()));
    }

    // no duplicated ids allowed
    List<Long> duplicatedIds = updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .collect(Collectors.groupingBy(ModifiableQuestionDTO::id, Collectors.counting()))
        .entrySet()
        .stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .toList();
    for (Long id : duplicatedIds) {
      errors.add(new ErrorDTO(QuestionErrorType.DUPLICATED_ID, id, QuestionErrorType.DUPLICATED_ID.getMsg()));
    }

    // apply changes if no errors found
    if (errors.isEmpty()) {
      for (ModifiableQuestionDTO item : updateQuestionsDTO.modifiableQuestionDTOList()) {
        Question question = dao.getQuestionById(item.id());
        question.setPosition(item.position());
        question.setActive(item.isActive());
        dao.save(question);
      }
    }

    return new ErrorListDTO(errors);
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

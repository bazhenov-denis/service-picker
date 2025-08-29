package com.example.backend.services;

import com.example.backend.DAO.OptionDao;
import com.example.backend.DAO.OptionScoreDao;
import com.example.backend.DAO.QuestionDao;
import com.example.backend.DAO.ScoreTypeDao;
import com.example.backend.DTO.questionsDTO.AdminQuestionDTO;
import com.example.backend.DTO.questionsDTO.CreateOptionDTO;
import com.example.backend.DTO.questionsDTO.CreateQuestionDTO;
import com.example.backend.DTO.questionsDTO.QuestionExceptionDTO;
import com.example.backend.DTO.questionsDTO.QuestionExceptionListDTO;
import com.example.backend.DTO.questionsDTO.ModifiableQuestionDTO;
import com.example.backend.DTO.questionsDTO.OptionDTO;
import com.example.backend.DTO.questionsDTO.OptionScoreDTO;
import com.example.backend.DTO.questionsDTO.QuestionDTO;
import com.example.backend.DTO.questionsDTO.UpdateQuestionsDTO;
import com.example.backend.enums.QuestionExceptionType;
import com.example.backend.enums.QuestionReference;
import com.example.backend.enums.QuestionType;
import com.example.backend.enums.ScoreCode;
import com.example.backend.exceptions.QuestionException;
import com.example.backend.models.Option;
import com.example.backend.models.OptionScore;
import com.example.backend.models.Question;
import com.example.backend.models.ScoreType;
import java.util.ArrayList;
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
    if (!QuestionType.contains(createQuestionDTO.type())) {
      throw new QuestionException(QuestionExceptionType.INVALID_TYPE, null);
    }

    if (createQuestionDTO.type().equals(QuestionType.REFERENCE.getType())) {
      if (!QuestionReference.contains(createQuestionDTO.referenceType())) {
        throw new QuestionException(QuestionExceptionType.INVALID_REFERENCE_TYPE, null);
      }
    } else if (createQuestionDTO.referenceType() != null) {
      throw new QuestionException(QuestionExceptionType.INVALID_REFERENCE_TYPE, null);
    }

    if (createQuestionDTO.type().equals(QuestionType.SINGLE_CHOICE.getType())
        || createQuestionDTO.type().equals(QuestionType.MULTIPLE_CHOICE.getType())) {
      if (createQuestionDTO.options().size() < 2) {
        throw new QuestionException(QuestionExceptionType.NOT_ENOUGH_OPTIONS, null);
      }
    }

    for (CreateOptionDTO createOptionDTO : createQuestionDTO.options()) {
      if (!ScoreCode.contains(createOptionDTO.getScore().code())) {
        throw new QuestionException(QuestionExceptionType.WRONG_SCORE_CODE, null);
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
        Option option = new Option();
        OptionScore optionScore = new OptionScore();
        ArrayList<OptionScore> optionScores = new ArrayList<>();
        optionScores.add(optionScore);

        optionScore.setScoreType(scoreTypeDao.findByCode(createQuestionDTO.options().get(i).getScore().code()));
        optionScore.setWeight(createQuestionDTO.options().get(i).getScore().weight());
        optionScore.setOption(option);

        option.setPosition(i + 1);
        option.setText(createQuestionDTO.options().get(i).getText());
        option.setOptionScores(optionScores);
        option.setQuestion(question);

        options.add(option);
      }

      question.setOptions(options);
    }

    dao.save(question);

    return question.toDto();
  }

  @Transactional
  public QuestionExceptionListDTO updateQuestions(UpdateQuestionsDTO updateQuestionsDTO) {
    List<QuestionExceptionDTO> exceptions = new ArrayList<>();

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
      exceptions.add(new QuestionExceptionDTO(QuestionExceptionType.WRONG_ORDER, null, QuestionExceptionType.WRONG_ORDER.getMsg()));
    }

    // question id must exist
    long maxId = dao.getMaxId();
    updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .map(ModifiableQuestionDTO::id)
        .filter(e -> e < 1 || e > maxId)
        .forEach(e -> exceptions.add(new QuestionExceptionDTO(QuestionExceptionType.WRONG_ID, e, QuestionExceptionType.WRONG_ID.getMsg())));

    // question position must fit order
    int maxPosition = dao.getNextPosition() - 1;
    updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .filter(e -> e.position() < 1 || e.position() > maxPosition)
        .forEach(
            e -> exceptions.add(
                new QuestionExceptionDTO(QuestionExceptionType.WRONG_POSITION, e.id(), QuestionExceptionType.WRONG_POSITION.getMsg())
            )
        );

/*    // first 3 questions are unmodifiable
    updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .map(ModifiableQuestionDTO::id)
        .filter(e -> e < 4 && e > 0)
        .forEach(
            e -> exceptions.add(
                new QuestionExceptionDTO(QuestionExceptionType.UNMODIFIABLE_QUESTION, e, QuestionExceptionType.UNMODIFIABLE_QUESTION.getMsg())
            )
        );*/

    // no duplicated ids allowed
    updateQuestionsDTO.modifiableQuestionDTOList()
        .stream()
        .collect(Collectors.groupingBy(ModifiableQuestionDTO::id, Collectors.counting()))
        .entrySet()
        .stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .forEach(
            e -> exceptions.add(
                new QuestionExceptionDTO(QuestionExceptionType.DUPLICATED_ID, e, QuestionExceptionType.DUPLICATED_ID.getMsg())
            )
        );

    // apply changes if no errors found
    if (exceptions.isEmpty()) {
      for (ModifiableQuestionDTO item : updateQuestionsDTO.modifiableQuestionDTOList()) {
        Question question = dao.getQuestionById(item.id());
        question.setPosition(item.position());
        question.setActive(item.isActive());
        dao.save(question);
      }
    }

    return new QuestionExceptionListDTO(exceptions);
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

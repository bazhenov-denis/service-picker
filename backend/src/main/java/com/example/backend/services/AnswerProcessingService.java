package com.example.backend.services;

import com.example.backend.DAO.QuestionDao;
import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.ProcessingResult;
import com.example.backend.DTO.VacancyResult;
import com.example.backend.enums.ScoreCode;
import com.example.backend.models.OptionScore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AnswerProcessingService {

  private static final Logger log = LoggerFactory.getLogger(AnswerProcessingService.class);

  private final QuestionDao questionDao;
  private final PriceProfroleService priceProfroleService;
  private final PriceRegionService priceRegionService;
  private final HhApiService apiService;

  public AnswerProcessingService(
      QuestionDao questionDao,
      PriceProfroleService priceProfroleService,
      PriceRegionService priceRegionService,
      HhApiService apiService
  ) {
    this.questionDao = questionDao;
    this.priceProfroleService = priceProfroleService;
    this.priceRegionService = priceRegionService;
    this.apiService = apiService;
  }

  public ProcessingResult process(ClaimDto claim) {
    Map<String, List<String>> answer = claim.getCodes();
    log.info("Входные ответы: {}", answer);

    List<Long> questionIds = answer.keySet().stream()
        .map(Long::valueOf)
        .toList();

    List<String> types = questionDao.findTypesByQuestionIds(questionIds);

    Map<Long, String> idToTypeMap = IntStream.range(0, questionIds.size())
        .boxed()
        .collect(Collectors.toMap(questionIds::get, types::get));

    ProcessingResult result = new ProcessingResult();
    List<Long> regionIds = new ArrayList<>();
    List<Long> profRoleIds = new ArrayList<>();

    for (var entry : idToTypeMap.entrySet()) {
      Long id = entry.getKey();
      String type = entry.getValue();
      List<String> values = answer.get(String.valueOf(id));

      switch (type) {
        case "reference" -> handleReference(id, values, result, regionIds, profRoleIds);
        case "input" -> handleInput(values, result);
        case "single-choice" -> handleChoice(values, result);
        default -> log.warn("Неизвестный тип вопроса '{}' для ID {}", type, id);
      }
    }

    if (!regionIds.isEmpty() && !profRoleIds.isEmpty()) {
      int competition;
      try {
        VacancyResult vacancy = apiService.getVacancyCount(
            regionIds.get(0).intValue(),
            profRoleIds.get(0).intValue()
        );
        competition = vacancy.count();
      } catch (Exception e) {
        log.warn("Не удалось получить количество вакансий, устанавливаю competition=50", e);
        competition = 50;
      }
      result.addScore(ScoreCode.COMPETITION, competition);
    }
    return result;
  }

  private void handleInput(List<String> values, ProcessingResult result) {
    if (values == null || values.isEmpty()) {
      return;
    }

    try {
      long quantity = Long.parseLong(values.get(0));
      result.addScore(ScoreCode.MASS, quantity);
    } catch (NumberFormatException e) {
      log.warn("Incorrect input");
    }
  }

  private void handleChoice(List<String> values, ProcessingResult result) {

    if (values == null || values.isEmpty()) {
      return;
    }

    List<Long> optionIds = values.stream().map(s -> {
          try {
            return Long.parseLong(s);
          } catch (NumberFormatException ex) {
            log.warn("Incorrect optionId");
            return null;
          }
        })
        .filter(Objects::nonNull)
        .toList();

    if (optionIds.isEmpty()) {
      return;
    }
    List<OptionScore> allScores = questionDao.findScoresByOptionIds(optionIds);
    for (OptionScore os : allScores) {
      ScoreCode code = ScoreCode.fromCode(os.getScoreType().getCode());
      if (code != null) {
        long weight = os.getWeight();
        result.addScore(code, weight);
      }
    }
  }

  private void handleReference(
      Long id,
      List<String> values,
      ProcessingResult result,
      List<Long> regionIds,
      List<Long> profRoleIds
  ) {
    String refType = questionDao.findReferenceTypeByQuestionId(id).orElse("");
    if ("regions".equals(refType)) {
      for (String raw : values) {
        String[] parts = raw.split("\\.");
        long areaId = Long.parseLong(parts[1]);
        regionIds.add(areaId);
      }
      List<Long> priceRegion = priceRegionService.getAreaIdsByRegionIds(regionIds);
      result.setRegionIds(priceRegion);

    } else if ("professions".equals(refType)) {
      List<Long> list = values.stream()
          .map(Long::valueOf)
          .toList();
      profRoleIds.addAll(list);
      List<Long> priceProf = priceProfroleService.getPriceGroupsByProfroleId(list);
      result.setProfRoleIds(priceProf);

    } else {
      log.warn("Неизвестный referenceType = '{}'", refType);
    }
  }
}

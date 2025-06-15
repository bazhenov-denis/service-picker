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
  private final ApiService apiService;

  public AnswerProcessingService(QuestionDao questionDao,
      PriceProfroleService priceProfroleService,
      PriceRegionService priceRegionService,
      ApiService apiService) {
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
    log.info("ID вопросов: {}", questionIds);

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

      log.info("Обработка вопроса {} типа '{}' с ответами {}", id, type, values);
      switch (type) {
        case "reference"     -> handleReference(id, values, result, regionIds, profRoleIds);
        case "input"         -> handleInput(values, result);
        case "single-choice" -> handleChoice(values, result);
        default               -> log.warn("Неизвестный тип вопроса '{}' для ID {}", type, id);
      }
    }

    if (!regionIds.isEmpty() && !profRoleIds.isEmpty()) {
      log.info("Вызываем API подсчёта вакансий с регионами {} и профессиями {}", regionIds, profRoleIds);
      VacancyResult vacancy = apiService.getVacancyCount(
          regionIds.get(0).intValue(),
          profRoleIds.get(0).intValue()
      );
      log.info("API вернул количество вакансий = {}", vacancy.getCount());
      result.addScore(ScoreCode.COMPETITION, vacancy.getCount());
    }

    log.info("Итоговый ProcessingResult: {}", result);
    return result;
  }

  private void handleInput(List<String> values, ProcessingResult result) {
    log.debug("  [handleInput] получены значения {}", values);
    if (values == null || values.isEmpty()) return;

    try {
      long quantity = Long.parseLong(values.get(0));
      log.debug("  Распознано MASS = {}", quantity);
      result.addScore(ScoreCode.MASS, quantity);
    } catch (NumberFormatException e) {
      log.warn("  Не удалось распарсить MASS из '{}'", values.get(0));
    }
  }

  private void handleChoice(List<String> values, ProcessingResult result) {

    log.info("  [handleChoice] получены значения {}", values);
    if (values == null || values.isEmpty()) return;

    List<Long> optionIds = values.stream().map(s -> {
          try { return Long.parseLong(s); }
          catch (NumberFormatException ex) {
            log.warn("    Неверный optionId '{}'");
            return null;
          }
        })
        .filter(Objects::nonNull)
        .toList();

    log.info("    Распознанные optionIds = {}", optionIds);

    if (optionIds.isEmpty()) return;

    List<OptionScore> allScores = questionDao.findScoresByOptionIds(optionIds);

    log.info("    Загружены OptionScore = {}", allScores);

    for (OptionScore os : allScores) {
      ScoreCode code = ScoreCode.fromCode(os.getScoreType().getCode());
      if (code != null) {
        long weight = os.getWeight();

        log.info("      Добавляем балл {} = {}", code, weight);

        result.addScore(code, weight);
      }
    }
  }

  private void handleReference(Long id,
      List<String> values,
      ProcessingResult result,
      List<Long> regionIds,
      List<Long> profRoleIds) {
    log.debug("  [handleReference] вопрос {} значения {}", id, values);
    String refType = questionDao.findReferenceTypeByQuestionId(id).orElse("");
    log.debug("    referenceType = '{}'", refType);

    if ("regions".equals(refType)) {
      for (String raw : values) {
        String[] parts = raw.split("\\.");
        long areaId = Long.parseLong(parts[1]);
        regionIds.add(areaId);
        log.debug("      Распознан regionId = {}", areaId);
      }
      List<Long> priceRegion = priceRegionService.getAreaIdsByRegionIds(regionIds);
      log.debug("    Идентификаторы региональных цен = {}", priceRegion);
      result.setRegionIds(priceRegion);

    } else if ("professions".equals(refType)) {
      List<Long> list = values.stream()
          .map(Long::valueOf)
          .toList();
      profRoleIds.addAll(list);
      log.debug("    Распознанные profRoleIds = {}", list);
      List<Long> priceProf = priceProfroleService.getPriceGroupsByProfroleId(list);
      log.debug("    Идентификаторы цен по профессиям = {}", priceProf);
      result.setProfRoleIds(priceProf);

    } else {
      log.warn("    Неизвестный referenceType = '{}'", refType);
    }
  }
}

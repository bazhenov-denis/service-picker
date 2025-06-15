package com.example.backend.services;

import com.example.backend.DAO.OfferDao;
import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.OfferListDto;
import com.example.backend.DTO.ProcessingResult;
import com.example.backend.models.Offer;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceScoring implements OfferService {

  private static final Logger log = LoggerFactory.getLogger(OfferServiceScoring.class);
  private final AnswerProcessingService answerProcessingService;
  private final OfferDao offerDao;
  private final PriceRegionService priceRegionService;
  private final PriceProfroleService priceProfroleService;
  private final Random random = new Random();

  public OfferServiceScoring(AnswerProcessingService answerProcessingService,
      OfferDao offerDao,
      PriceRegionService priceRegionService,
      PriceProfroleService priceProfroleService) {
    this.answerProcessingService = answerProcessingService;
    this.offerDao = offerDao;
    this.priceRegionService = priceRegionService;
    this.priceProfroleService = priceProfroleService;
  }

  @Override
  public OfferListDto pick(ClaimDto claim) {
    // 1) Сначала скорим
    ProcessingResult result = answerProcessingService.process(claim);
    log.info("ProcessingResult: {}", result);
    log.info(claim.toString());

    // 2) Из ProcessingResult вытягиваем списки фильтров
    List<Long> regionIds = result.getRegionIds();
    List<Long> profroleIds = result.getProfRoleIds();

    // 3) Получаем все подходящие офферы
    List<Offer> offers = offerDao.getByRegionAndProfroleGroup(regionIds, profroleIds);
    if (offers.isEmpty()) {
      // нет ни одного — вернём пустой список
      return new OfferListDto();
    }

    // 4) Ищем самый дорогой
    Optional<Offer> bestOpt = offers.stream()
        .max(Comparator.comparingDouble(Offer::getPriceAll));

    Offer bestOffer = bestOpt.get();

    // 5) Генерим «случайные» vacancyType и apiLimitedCount
    String[] types = {"open", "closed", "limited"};
    String vacancyType = types[random.nextInt(types.length)];
    String apiLimitedCount = String.valueOf(10 + random.nextInt(91)); // от 10 до 100

    // 6) Составляем OfferDto (как в вашем примере)
    OfferDto dto = new OfferDto(
        "Оптимальный",                                      // label
        bestOffer.getCode(),                                // type
        bestOffer.getTariff(),                              // title
        bestOffer.getPeriod().toString(),                   // period
        priceRegionService.getRegionNameById(bestOffer.getRegionId()),
        priceProfroleService.getProfroleNameById(
            bestOffer.getProfroleGroupId().longValue()
        ),
        Double.toString(bestOffer.getPriceAll() / 100.0),   // цена в рублях
        vacancyType,
        String.valueOf(bestOffer.getChildCount1()),        // vacancyCount
        String.valueOf(bestOffer.getChildCount2()),        // civCount
        apiLimitedCount
    );
    OfferListDto offerListDto = new OfferListDto();
    offerListDto.add(dto);
    return offerListDto;
  }
}

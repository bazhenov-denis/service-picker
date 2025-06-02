package com.example.backend.services;

import com.example.backend.DAO.OfferDaoImpl;
import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.OfferListDto;
import com.example.backend.enums.OfferChildCodeEnum;
import com.example.backend.models.Offer;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.backend.models.PriceProfrole;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService {

  private static final Logger log = LoggerFactory.getLogger(OfferService.class);

  private final OfferDaoImpl offerDao;
  private final ApiService apiService;
  private final PriceProfroleService priceProfroleService;
  private final PriceRegionService priceRegionService;

  public OfferService(
      OfferDaoImpl offerDao,
      ApiService apiService,
      PriceProfroleService priceProfroleService,
      PriceRegionService priceRegionService
  ) {
    this.offerDao = offerDao;
    this.apiService = apiService;
    this.priceProfroleService = priceProfroleService;
    this.priceRegionService = priceRegionService;
  }

  private static final Logger log = LoggerFactory.getLogger(OfferService.class);

  public OfferListDto pick(ClaimDto claim) {
    log.info("Начало pick(), claim = {}", claim);
    // variables for querying
    Integer areaId = claim.getAreaId();
    Integer professionId = claim.getProfessionId();
    Integer quantity = claim.getQuantity();
    Integer period = claim.getPeriod();
    log.info("OfferService IN, claim = {}", claim);

    log.info(
        "Параметры: areaId={}, professionId={}, quantity={}, period={}",
        areaId, professionId, quantity, period
    );
    // поход в API за кол-вом вакансий по региону и профессии
    Integer vacancyCount = apiService.getVacancyCount(areaId, professionId).getCount();
    log.info("vacancyCount = {}", vacancyCount);
    List<Long> areas = new ArrayList<>();
    areas.add(areaId.longValue());
    log.info("areas = {}", areas);
    List<Long> priceRegion = priceRegionService.getAreaIdsByRegionIds(areas);
    log.info("priceRegion IDs = {}", priceRegion);

    List<Long> priceProfrole = priceProfroleService.getPriceGroupsByProfroleId(professionId.longValue())
        .stream()
        .map(PriceProfrole::getId)
        .toList();
    log.info("priceProfrole IDs = {}", priceProfrole);

    Set<Integer> allowedPeriods;
    if (period == 1) {
      allowedPeriods = Set.of(1, 7);
    } else if (period == 7) {
      allowedPeriods = Set.of(7, 14, 30);
    } else if (period == 30) {
      allowedPeriods = Set.of(14, 30);
    } else if (period == Integer.MAX_VALUE) {
      allowedPeriods = Set.of(30, 92, 183, 365);
    } else {
      allowedPeriods = null;
    }

    // алгоритм подбора
    List<Offer> offers = offerDao.getByRegionAndProfroleGroup(priceRegion, priceProfrole)
        .parallelStream()
        .filter(quantity * vacancyCount > 100 ? o -> (o.getCode().equals("DI") || o.getCode().equals("CIV+VPPL")) : o -> true)
        .filter(allowedPeriods == null ? o -> true : o -> allowedPeriods.contains(o.getPeriod()))
        .sorted(Comparator.comparing(Offer::getPriceAll))
        .toList();

    log.info("Найдено offers.size() = {}", offers.size());

    // controller returns 404 if no offer was found
    if (offers.isEmpty()) {
      log.info("Offers не найдены, возвращаем пустой OfferListDto");
      return new OfferListDto();
    }

    Offer bestOffer = offers.get(0);
    OfferListDto offerListDto = new OfferListDto();
    log.info(
        "Выбран bestOffer: id={}, code={}, price={}",
        bestOffer.getId(), bestOffer.getCode(), bestOffer.getPriceAll()
    );


    // put offer in DTO
    if (bestOffer.getCode().equals("DI")) {
      OfferDto offerDto = new OfferDto(
          "Оптимальный",
          "resume_access",
          "Доступ к базе резюме",
          bestOffer.getPeriod().toString(),
          priceRegionService.getRegionNameById(bestOffer.getRegionId()),
          priceProfroleService.getProfroleNameById(bestOffer.getProfroleGroupId().longValue()),
          Double.toString(bestOffer.getPriceAll() / 100.0),
          null,
          null,
          bestOffer.getChildCount1().toString(),
          bestOffer.getChildCount2().toString()
      );
      offerListDto.add(offerDto);
    }
    if (bestOffer.getCode().equals("VPPL")) {
      OfferDto offerDto = new OfferDto(
          "Оптимальный",
          "vacancy",
          "Публикация вакансий",
          bestOffer.getPeriod().toString(),
          priceRegionService.getRegionNameById(bestOffer.getRegionId()),
          priceProfroleService.getProfroleNameById(bestOffer.getProfroleGroupId().longValue()),
          Double.toString(bestOffer.getPriceAll() / 100.0),
          OfferChildCodeEnum.getLabelByCode(bestOffer.getChildCode1()),
          bestOffer.getChildCount1().toString(),
          null,
          null
      );
      offerListDto.add(offerDto);
    }
    if (bestOffer.getCode().equals("CIV+VPPL")) {
      OfferDto offerDto = new OfferDto(
          "Оптимальный",
          "mixed",
          "Доступ к базе резюме + публикация вакансий",
          bestOffer.getPeriod().toString(),
          priceRegionService.getRegionNameById(bestOffer.getRegionId()),
          priceProfroleService.getProfroleNameById(bestOffer.getProfroleGroupId().longValue()),
          Double.toString(bestOffer.getPriceAll() / 100.0),
          OfferChildCodeEnum.getLabelByCode(bestOffer.getChildCode2()),
          bestOffer.getChildCount2().toString(),
          bestOffer.getChildCount1().toString(),
          bestOffer.getChildCount3().toString()
      );
      offerListDto.add(offerDto);
    }

    log.info("Готовый OfferListDto = {}", offerListDto);
    return offerListDto;
  }
}

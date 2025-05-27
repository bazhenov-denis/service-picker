package com.example.backend.services;

import com.example.backend.DAO.OfferDaoImpl;
import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.OfferListDto;
import com.example.backend.DTO.VacancyResult;
import com.example.backend.models.Offer;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
  @Autowired
  private OfferDaoImpl offerDao;

  @Autowired
  private ApiService apiService;

  public OfferListDto pick(ClaimDto claim) {
    // variables for querying
    Integer areaId = claim.getAreaId();
    Integer professionId = claim.getProfessionId();
    Integer quantity = claim.getQuantity();
    Integer period = claim.getPeriod();

    // поход в API за кол-вом вакансий по региону и профессии
    VacancyResult vacancyResult = apiService.getVacancyCount(areaId, professionId);

    // алгоритм подбора
    List<Offer> offers = offerDao.getByRegionAndProfroleGroup(3000233L, 0)
        .parallelStream()
        .filter(quantity > 1 ? o -> (o.getCode().equals("DI") || o.getCode().equals("CIV+VPPL")) : o -> true)
        .filter(period != Integer.MAX_VALUE ? o -> o.getPeriod().equals(period) : o -> o.getPeriod() > 30)
        .sorted(Comparator.comparing(Offer::getPriceAll))
        .toList();

    // controller returns 404 if no offer was found
    if (offers.isEmpty()) return new OfferListDto();

    Offer bestOffer = offers.get(0);
    OfferListDto offerListDto = new OfferListDto();

    // put offer in DTO
    if (bestOffer.getCode().equals("DI")) {
      OfferDto offerDto = new OfferDto(
          "Оптимальный",
          "resume_access",
          "Доступ к базе резюме",
          bestOffer.getPeriod().toString(),
          bestOffer.getRegionId().toString(),
          bestOffer.getProfroleGroupId().toString(),
          bestOffer.getPriceAll().toString(),
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
          bestOffer.getRegionId().toString(),
          bestOffer.getProfroleGroupId().toString(),
          bestOffer.getPriceAll().toString(),
          bestOffer.getChildCode1(),
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
          bestOffer.getRegionId().toString(),
          bestOffer.getProfroleGroupId().toString(),
          bestOffer.getPriceAll().toString(),
          bestOffer.getChildCode2(),
          bestOffer.getChildCount2().toString(),
          bestOffer.getChildCount1().toString(),
          bestOffer.getChildCount3().toString()
      );
      offerListDto.add(offerDto);
    }

    return offerListDto;
  }
}

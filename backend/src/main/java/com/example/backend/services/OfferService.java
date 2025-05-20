package com.example.backend.services;

import com.example.backend.DAO.OfferDaoImpl;
import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.VacancyResult;
import com.example.backend.models.Offer;
import com.example.backend.DTO.ResumesAccessOfferDto;
import com.example.backend.DTO.VacancyOfferDto;

import java.util.ArrayList;
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

  public OfferDto pick(ClaimDto claim) {
    List<VacancyOfferDto> vacancyOfferDtos = new ArrayList<>();
    List<ResumesAccessOfferDto> resumesAccessOfferDtos = new ArrayList<>();

    // поход в API за кол-вом вакансий по региону и профессии
    VacancyResult vacancyResult = apiService.getVacancyCount(claim.areaId(), claim.professionId());

    // алгоритм подбора
    List<Offer> offers = offerDao.getByRegionAndProfroleGroup(0L, 0);
    offers.sort(Comparator.comparing(Offer::getPriceAll));
    Offer bestOffer = offers.get(0);

    if (bestOffer.getCode().equals("DI")) {
      resumesAccessOfferDtos.add(new ResumesAccessOfferDto(bestOffer.getChildCount1(), bestOffer.getChildCount2()));
    }
    if (bestOffer.getCode().equals("VPPL")) {
      vacancyOfferDtos.add(new VacancyOfferDto(bestOffer.getChildCode1(), bestOffer.getChildCount1()));
    }

    return new OfferDto(vacancyOfferDtos, resumesAccessOfferDtos);
  }
}

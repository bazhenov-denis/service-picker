package com.example.backend.services;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.VacancyResult;
import com.example.backend.models.ResumesAccessOffer;
import com.example.backend.models.VacancyOffer;
import com.example.backend.service.ApiService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
  @Autowired
  private ApiService apiService;

  public OfferDto pick(ClaimDto claim) {
    Map<Integer, VacancyOffer> vacancyOffers = new HashMap<>();
    Map<Integer, ResumesAccessOffer> resumesAccessOffers = new HashMap<>();

    // поход в API за кол-вом вакансий по региону и профессии
    VacancyResult vacancyResult = apiService.getVacancyCount(claim.areaId(), claim.professionId());

    // алгоритм подбора
    if (vacancyResult.isSuccess()) {
      if (vacancyResult.getCount() < 100){
        vacancyOffers.put(
            1, new VacancyOffer(
                claim.areaId(),
                claim.professionId(),
                50,
                "Regular",
                30,
                100.0,
                5000.0
            )
        );
      } else {
        resumesAccessOffers.put(
            1, new ResumesAccessOffer(
                claim.areaId(),
                claim.professionId(),
                30,
                100,
                4500.0
            )
        );
      }
    }

    return new OfferDto(vacancyOffers, resumesAccessOffers);
  }
}

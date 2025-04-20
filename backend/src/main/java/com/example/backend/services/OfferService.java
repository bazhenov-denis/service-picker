package com.example.backend.services;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.VacancyResult;
import com.example.backend.models.ResumesAccessOffer;
import com.example.backend.models.VacancyOffer;
import com.example.backend.repositories.ResumesAccessOfferRepository;
import com.example.backend.repositories.VacancyOfferRepository;
import com.example.backend.services.ApiService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
  @Autowired
  private ApiService apiService;

  @Autowired
  private ResumesAccessOfferRepository resumesAccessOfferRepository;

  @Autowired
  private VacancyOfferRepository vacancyOfferRepository;

  public OfferDto pick(ClaimDto claim) {
    List<VacancyOffer> vacancyOffers = new ArrayList<>();
    List<ResumesAccessOffer> resumesAccessOffers = new ArrayList<>();

    // поход в API за кол-вом вакансий по региону и профессии
    VacancyResult vacancyResult = apiService.getVacancyCount(claim.areaId(), claim.professionId());

    // алгоритм подбора
    if (vacancyResult.isSuccess()) {
      if (vacancyResult.getCount() < 100) {
        // если искомая услуга есть в базе, то добавляем ее в ответ
        // если нет, то ничего не добавляем
        Optional<VacancyOffer> vacancyOffer = vacancyOfferRepository.findById(1L);
        vacancyOffer.ifPresent(vacancyOffers::add);
      } else {
        Optional<ResumesAccessOffer> resumesAccessOffer = resumesAccessOfferRepository.findById(1L);
        resumesAccessOffer.ifPresent(resumesAccessOffers::add);
      }
    } else {
      // если обращение в апи неудачно, то возвращаем пустой ДТО, контроллер вернет 400 статус
      return new OfferDto(Collections.emptyList(), Collections.emptyList());
    }

    return new OfferDto(vacancyOffers, resumesAccessOffers);
  }
}

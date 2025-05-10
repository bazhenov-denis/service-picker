package com.example.backend.services;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.VacancyResult;
import com.example.backend.models.Offer;
import com.example.backend.models.ResumesAccessOffer;
import com.example.backend.models.VacancyOffer;
import com.example.backend.repositories.ResumesAccessOfferRepository;
import com.example.backend.repositories.VacancyOfferRepository;
import com.example.backend.services.ApiService;
import com.example.backend.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
    Session session = HibernateUtil.getSessionFactory().openSession();
    //Transaction tx = session.beginTransaction();
    Query query = session.createQuery("SELECT * FROM offers");
    List<Offer> offers = (List<Offer>)query.list();
    session.close();

    return new OfferDto(vacancyOffers, resumesAccessOffers);
  }
}

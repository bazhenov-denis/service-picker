package com.example.backend.services;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.DTO.VacancyResult;
import com.example.backend.models.Offer;
import com.example.backend.DTO.ResumesAccessOfferDto;
import com.example.backend.DTO.VacancyOfferDto;
import com.example.backend.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
  @Autowired
  private ApiService apiService;

  public OfferDto pick(ClaimDto claim) {
    List<VacancyOfferDto> vacancyOfferDtos = new ArrayList<>();
    List<ResumesAccessOfferDto> resumesAccessOfferDtos = new ArrayList<>();

    // поход в API за кол-вом вакансий по региону и профессии
    VacancyResult vacancyResult = apiService.getVacancyCount(claim.areaId(), claim.professionId());

    // алгоритм подбора
    Session session = HibernateUtil.getSessionFactory().openSession();
    //Transaction tx = session.beginTransaction();
    Query query = session.createQuery("SELECT * FROM offers");
    List<Offer> offers = (List<Offer>)query.list();
    session.close();

    return new OfferDto(vacancyOfferDtos, resumesAccessOfferDtos);
  }
}

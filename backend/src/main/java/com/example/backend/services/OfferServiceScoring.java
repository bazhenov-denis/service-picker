package com.example.backend.services;

import com.example.backend.DAO.OfferDaoImpl;
import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferListDto;
import com.example.backend.DTO.ProcessingResult;
import com.example.backend.handlers.CriteriaHandler;
import com.example.backend.models.Offer;
import com.example.backend.query.OfferMapper;
import com.example.backend.query.QueryBuilder;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceScoring implements OfferService {

  private static final Logger log = LoggerFactory.getLogger(OfferServiceScoring.class);
  private final AnswerProcessingService answerProcessingService;
  private final List<CriteriaHandler> handlers;
  private final OfferMapper offerMapper;
  private final OfferDaoImpl offerDaoImpl;

  public OfferServiceScoring(
      AnswerProcessingService answerProcessingService,
      List<CriteriaHandler> handlers, OfferMapper offerMapper,
      OfferDaoImpl offerDaoImpl
  ) {
    this.answerProcessingService = answerProcessingService;
    this.handlers = handlers;

    this.offerMapper = offerMapper;
    this.offerDaoImpl = offerDaoImpl;
  }

  @Override
  public OfferListDto pick(ClaimDto claim) {
    log.info(claim.toString());
    ProcessingResult result = answerProcessingService.process(claim);
    log.info("ProcessingResult: {}", result);


    QueryBuilder qb = new QueryBuilder();
    for (CriteriaHandler handler : handlers) {
      handler.apply(result, qb);
    }
    log.debug("SQL: {}", qb.buildSql());
    log.debug("Params: {}", qb.getParams());
    List<Offer> offers = offerDaoImpl.findOffers(qb.buildSql(), qb.getParams());

    offers.sort(Comparator.comparingDouble(Offer::getPriceAll));

    OfferListDto dtoList = new OfferListDto();
    offers.stream()
        .map(offerMapper::toDto)
        .forEach(dtoList::add);

    return dtoList;

  }
}

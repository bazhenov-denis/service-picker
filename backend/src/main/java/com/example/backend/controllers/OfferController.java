package com.example.backend.controllers;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfferController {
  @Autowired
  private OfferService offerService;

  @PostMapping("/service-offer")
  public ResponseEntity<OfferDto> processClaim(@Validated @RequestBody ClaimDto claim) {
    // обращение в сервис подбора
    OfferDto offer = offerService.pick(claim);

    // пустой ответ от сервиса => что-то пошло не так => 400
    if (offer.vacancyOffers().isEmpty() && offer.resumesAccessOffers().isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // 200
    return ResponseEntity.ok(offer);
  }
}

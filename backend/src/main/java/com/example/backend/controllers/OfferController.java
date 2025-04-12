package com.example.backend.controllers;

import com.example.backend.models.ResumesAccessOffer;
import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.models.VacancyOffer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfferController {
  @PostMapping("/service-offer")
  public ResponseEntity<OfferDto> processRequest(@RequestBody ClaimDto request) {

    Map<Integer, VacancyOffer> vacancyServices = new HashMap<>();
    Map<Integer, ResumesAccessOffer> resumesAccessServices = new HashMap<>();

    vacancyServices.put(1, new VacancyOffer(
        request.areaId(),
        request.professionId(),
        50,
        "Regular",
        30,
        100.0,
        5000.0
        ));
    resumesAccessServices.put(1, new ResumesAccessOffer(
        request.areaId(),
        request.professionId(),
        30,
        100,
        4500.0
        ));

    return ResponseEntity.ok(new OfferDto(vacancyServices, resumesAccessServices));
  }
}

package com.example.backend.controllers;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferDto;
import com.example.backend.services.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "Pick an offer by user data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully picked a service",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = OfferDto.class))
          }),
      @ApiResponse(responseCode = "400", description = "Invalid user data supplied", content = @Content)
  })
  @PostMapping("/service-offer")
  public ResponseEntity<OfferDto> processClaim(@io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "User claim data", required = true, content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ClaimDto.class), examples = @ExampleObject(
              value = "{ \"professionId\": 5, \"amount\": 2, \"areaId\": 1 }"
          ))
  )
      @Validated @RequestBody ClaimDto claim) {
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

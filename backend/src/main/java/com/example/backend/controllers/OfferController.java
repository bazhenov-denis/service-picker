package com.example.backend.controllers;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferListDto;
import com.example.backend.services.OfferServiceScoring;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OfferController {

  private final OfferServiceScoring offerService;

  public OfferController(OfferServiceScoring offerService) {
    this.offerService = offerService;
  }

  @Operation(summary = "Pick offers by user data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully picked services",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = OfferListDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid user data supplied", content = @Content)
  })
  @PostMapping("/service-offer")
  public ResponseEntity<OfferListDto> processClaim(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "User claim data", required = true, content = @Content(
          mediaType = "application/json", schema = @Schema(implementation = ClaimDto.class), examples = @ExampleObject(
          value = "{ \"professionId\": 5, \"amount\": 2, \"areaId\": 1 }"
      )))
      @RequestBody ClaimDto claim
  ) {
    OfferListDto offerListDto = offerService.pick(claim);
    if (offerListDto.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return ResponseEntity.ok(offerListDto);
  }
}

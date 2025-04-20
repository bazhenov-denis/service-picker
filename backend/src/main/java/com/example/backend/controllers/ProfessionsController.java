package com.example.backend.controllers;

import com.example.backend.DTO.ProfessionalRolesResponseDTO;
import com.example.backend.services.ApiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/professions")
public class ProfessionsController {
  private final ApiService apiService;

  public ProfessionsController(ApiService apiService) {
    this.apiService = apiService;
  }

  @Operation(summary = "Get professions tree from api.hh.ru")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully got professions tree",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ProfessionalRolesResponseDTO.class),
                  examples = @ExampleObject(
                      value = "{ \"categories\": [{\"id\": \"19\", \"name\": \"Автомобильный бизнес\", " +
                          "\"roles\": [{\"id\": \"4\", \"name\": \"Автомойщик\"}]}]}"
                  )),
          }),
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @GetMapping
  public ResponseEntity<ProfessionalRolesResponseDTO> getProfessions() {
    try {
      ProfessionalRolesResponseDTO dto = apiService.getProfessionalRolesDictionary();
      return ResponseEntity.ok(dto);
    } catch (IllegalStateException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Некорректный запрос: " + e.getMessage(),
          e
      );
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Неожиданная ошибка",
          e
      );
    }
  }
}
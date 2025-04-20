package com.example.backend.controllers;

import com.example.backend.DTO.AreaDTO;
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

import java.util.List;

@RestController
@RequestMapping("/areas")
public class AreasController {
  private final ApiService apiService;

  public AreasController(ApiService apiService) {
    this.apiService = apiService;
  }

  @Operation(summary = "Get areas tree from api.hh.ru")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully got areas tree",
          content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = AreaDTO.class),
                  examples = @ExampleObject(
                      value = "{ \"id\": \"113\", \"parent_id\": null, \"name\": \"Россия\", \"areas\": [] }"
                  ))
          }),
      @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
  })
  @GetMapping
  public ResponseEntity<List<AreaDTO>> getAllAreas() {
    try {
      List<AreaDTO> areas = apiService.getAreas();
      return ResponseEntity.ok(areas);
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

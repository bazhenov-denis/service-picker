package com.example.backend.controllers;

import com.example.backend.DTO.ProfessionalRolesResponseDTO;
import com.example.backend.services.ApiService;

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
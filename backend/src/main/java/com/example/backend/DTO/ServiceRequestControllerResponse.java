package com.example.backend.DTO;

import java.util.Map;

public record ServiceRequestControllerResponse(Map<Integer, VacancyServiceDTO> vacancyServices, Map<Integer, ResumesAccessServiceDTO> resumesAccessServices) {
}

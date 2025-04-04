package com.example.backend.models;

import java.util.Map;

public record ServiceResponse(Map<Integer, VacancyService> vacancyServices, Map<Integer, ResumesAccessService> resumesAccessServices) {
}

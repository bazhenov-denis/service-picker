package com.example.backend.DTO;

import com.example.backend.models.ResumesAccessOffer;
import com.example.backend.models.VacancyOffer;
import java.util.Map;

public record OfferDto(Map<Integer, VacancyOffer> vacancyServices, Map<Integer, ResumesAccessOffer> resumesAccessServices) {
}

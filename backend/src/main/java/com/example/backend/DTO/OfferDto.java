package com.example.backend.DTO;

import java.util.List;

public record OfferDto(List<VacancyOfferDto> vacancyOfferDtos, List<ResumesAccessOfferDto> resumesAccessOfferDtos) {
}

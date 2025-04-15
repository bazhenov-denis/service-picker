package com.example.backend.DTO;

import com.example.backend.models.ResumesAccessOffer;
import com.example.backend.models.VacancyOffer;
import java.util.List;

public record OfferDto(List<VacancyOffer> vacancyOffers, List<ResumesAccessOffer> resumesAccessOffers) {
}

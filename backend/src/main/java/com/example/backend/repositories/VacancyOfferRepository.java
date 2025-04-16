package com.example.backend.repositories;

import com.example.backend.models.VacancyOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyOfferRepository extends JpaRepository<VacancyOffer, Long> {
}
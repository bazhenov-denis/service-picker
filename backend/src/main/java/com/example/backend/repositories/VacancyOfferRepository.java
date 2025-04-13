package com.example.backend.repositories;

import com.example.backend.entities.VacancyOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyOfferRepository extends JpaRepository<VacancyOfferEntity, Long> {
}
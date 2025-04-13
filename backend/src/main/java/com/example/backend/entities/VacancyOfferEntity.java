package com.example.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "vacancy_offer")
public class VacancyOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer areaId;

    private Integer professionId;

    private Integer packageVolume;

    private String vacancyType;

    private Integer publicationPeriod;

    private Double pricePerOne;

    private Double pricePerPackage;
}

package com.example.backend.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacancy_offer")
public class VacancyOffer {

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

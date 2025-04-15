package com.example.backend.models;

public record VacancyOffer(
    Integer areaId,
    Integer professionId,
    Integer volumeOfPublicationPackage,
    String vacancyType,
    Integer publicationPeriod,
    Double pricePerOne,
    Double pricePerPackage
) {
}

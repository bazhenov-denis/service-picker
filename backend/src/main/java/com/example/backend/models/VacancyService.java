package com.example.backend.models;

public record VacancyService(
    String area,
    String profession,
    Integer volumeOfPublicationPackage,
    String vacancyType,
    Integer publicationPeriod,
    Double pricePerOne,
    Double pricePerPackage
) {
}

package com.example.backend.DTO;

public record VacancyServiceDTO(
    Integer areaId,
    Integer professionId,
    Integer volumeOfPublicationPackage,
    String vacancyType,
    Integer publicationPeriod,
    Double pricePerOne,
    Double pricePerPackage
) {
}

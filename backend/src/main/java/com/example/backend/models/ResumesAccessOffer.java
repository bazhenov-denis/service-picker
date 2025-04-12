package com.example.backend.models;

public record ResumesAccessOffer(
    Integer areaId,
    Integer professionId,
    Integer accessDuration,
    Integer numberOfContacts,
    Double price
) {
}

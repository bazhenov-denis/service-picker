package com.example.backend.models;

public record ResumesAccessService(
    String area,
    String profession,
    Integer accessDuration,
    Integer numberOfContacts,
    Double price
) {
}

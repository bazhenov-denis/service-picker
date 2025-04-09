package com.example.backend.DTO;

public record ResumesAccessServiceDTO(
    Integer areaId,
    Integer professionId,
    Integer accessDuration,
    Integer numberOfContacts,
    Double price
) {
}

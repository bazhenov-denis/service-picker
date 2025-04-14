package com.example.backend.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record ClaimDto(
    @NotNull @Min(1) Integer professionId,
    @NotNull @Min(1) Integer amount,
    @NotNull @Min(1) Integer areaId
) {
}


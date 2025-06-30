package com.example.backend.DTO;

import java.util.List;

public record ProfessionalRolesResponseDTO(
    List<ProfessionalRoleCategoryDTO> categories
) {
}

package com.example.backend.DTO;

import java.util.List;

public class ProfessionalRolesResponseDTO {
    private List<ProfessionalRoleCategoryDTO> categories;

    public List<ProfessionalRoleCategoryDTO> getCategories() {
        return categories;
    }
    public void setCategories(List<ProfessionalRoleCategoryDTO> categories) {
        this.categories = categories;
    }
}

package com.example.backend.DTO;

import java.util.List;

public class ProfessionalRoleCategoryDTO {
    private String id;
    private String name;
    private List<ProfessionalRoleDTO> roles;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<ProfessionalRoleDTO> getRoles() {
        return roles;
    }
    public void setRoles(List<ProfessionalRoleDTO> roles) {
        this.roles = roles;
    }
}

package com.example.backend.controllers;

import com.example.backend.DTO.ProfessionalRoleCategoryDTO;
import com.example.backend.DTO.ProfessionalRolesResponseDTO;
import com.example.backend.DTO.ProfessionalRoleDTO;
import com.example.backend.services.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professions")
public class GetProfessionsResource {

    @Autowired
    private ApiService apiService;

    @GetMapping
    public String getProfessions() {
        ProfessionalRolesResponseDTO rolesResponse = apiService.getProfessionalRolesDictionary(); 
        return transformProfessionalRoles(rolesResponse).toString();
    }

    private JSONObject transformProfessionalRoles(ProfessionalRolesResponseDTO response) {
        JSONArray transformedCategories = new JSONArray();
        for (ProfessionalRoleCategoryDTO category : response.getCategories()) {
            JSONObject catObj = new JSONObject();
            catObj.put("category_id", category.getId());
            catObj.put("name", category.getName());

            JSONArray rolesArray = new JSONArray();
            for (ProfessionalRoleDTO role : category.getRoles()) {
                JSONObject roleObj = new JSONObject();
                roleObj.put("roles_id", role.getId());
                roleObj.put("name", role.getName());
                rolesArray.put(roleObj);
            }
            catObj.put("roles", rolesArray);
            transformedCategories.put(catObj);
        }

        JSONObject result = new JSONObject();
        result.put("categories", transformedCategories);
        return result;
    }
}

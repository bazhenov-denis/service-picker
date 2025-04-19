package com.example.backend.controllers;

import com.example.backend.DTO.AreaDTO;
import com.example.backend.services.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/areas")
public class GetAreasResource {

    @Autowired
    private ApiService apiService;

    @GetMapping
    public String getAreas() {
        List<AreaDTO> areas = apiService.getAreas();
        return transformAreas(areas).toString();
    }

    private JSONArray transformAreas(List<AreaDTO> areas) { // смена ключей
        JSONArray array = new JSONArray();
        for (AreaDTO area : areas) {
            JSONObject obj = new JSONObject();
            obj.put("area_id", area.getId());
            obj.put("name", area.getName());
            if (area.getAreas() != null && !area.getAreas().isEmpty()) {
                obj.put("areas", transformAreas(area.getAreas()));
            }
            array.put(obj);
        }
        return array;
    }
}

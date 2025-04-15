package com.example.backend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaDTO {
    private String id;
    private String name;
    private List<AreaDTO> areas; // вложенные области

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<AreaDTO> getAreas() { return areas; }
    public void setAreas(List<AreaDTO> areas) { this.areas = areas; }
}

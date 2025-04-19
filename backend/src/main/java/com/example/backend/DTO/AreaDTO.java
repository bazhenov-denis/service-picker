package com.example.backend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({ "id", "parent_id", "name", "areas" })
public class AreaDTO {

    private String id;

    @JsonProperty("parent_id")
    private String parentId;
    private String name;
    @JsonProperty("areas")
    private List<AreaDTO> areas; 

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getParentId() { return parentId; }
    public void   setParentId(String parentId) { this.parentId = parentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<AreaDTO> getAreas() { return areas; }
    public void setAreas(List<AreaDTO> areas) { this.areas = areas; }
}

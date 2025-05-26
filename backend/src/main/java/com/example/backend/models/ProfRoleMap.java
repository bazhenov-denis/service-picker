package com.example.backend.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "profroles_group_mapping")
public class ProfroleMap {

    @EmbeddedId
    private ProfroleMapId id;

    public ProfroleMap() {}

    public ProfroleMap(ProfroleMapId id) {
        this.id = id;
    }

    public ProfroleMapId getId() {
        return id;
    }

    public void setId(ProfroleMapId id) {
        this.id = id;
    }

    public Integer getPriceProfroleGroupId() {
        return id.getPriceProfroleGroupId();
    }

    public Integer getProfessionalRoleId() {
        return id.getProfessionalRoleId();
    }
}

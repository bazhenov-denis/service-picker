package com.example.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProfroleMapId implements Serializable {

    @Column(name = "price_profrole_group_id")
    private Integer priceProfroleGroupId;

    @Column(name = "professional_role_id")
    private Integer professionalRoleId;

    public ProfroleMapId() {}

    public ProfroleMapId(Integer priceProfroleGroupId, Integer professionalRoleId) {
        this.priceProfroleGroupId = priceProfroleGroupId;
        this.professionalRoleId = professionalRoleId;
    }

    public Integer getPriceProfroleGroupId() {
        return priceProfroleGroupId;
    }

    public void setPriceProfroleGroupId(Integer priceProfroleGroupId) {
        this.priceProfroleGroupId = priceProfroleGroupId;
    }

    public Integer getProfessionalRoleId() {
        return professionalRoleId;
    }

    public void setProfessionalRoleId(Integer professionalRoleId) {
        this.professionalRoleId = professionalRoleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfroleMapId)) return false;
        ProfroleMapId that = (ProfroleMapId) o;
        return Objects.equals(priceProfroleGroupId, that.priceProfroleGroupId) &&
                Objects.equals(professionalRoleId, that.professionalRoleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priceProfroleGroupId, professionalRoleId);
    }
}
package com.example.backend.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "profrole")
public class Profrole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "profroles")
    private Set<PriceProfrole> priceProfroles;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PriceProfrole> getPriceProfroles() {
        return priceProfroles;
    }

    public void setPriceProfroles(Set<PriceProfrole> priceProfroles) {
        this.priceProfroles = priceProfroles;
    }
}


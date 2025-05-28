package com.example.backend.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "price_profrole")
public class PriceProfrole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "price_profrole_profrole",
            joinColumns = @JoinColumn(name = "price_profrole_id"),
            inverseJoinColumns = @JoinColumn(name = "profrole_id")
    )
    private Set<Profrole> profroles;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Profrole> getProfroles() {
        return profroles;
    }

    public void setProfroles(Set<Profrole> profroles) {
        this.profroles = profroles;
    }
}

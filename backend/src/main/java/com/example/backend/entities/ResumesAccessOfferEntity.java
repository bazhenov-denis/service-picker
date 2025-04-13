package com.example.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "resumes_access_offer")
public class ResumesAccessOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer areaId;

    private Integer professionId;

    private Integer accessDuration;

    private Integer numberOfContacts;

    private Double price;
}

package com.example.backend.DAO;

import com.example.backend.models.PriceProfrole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PriceProfroleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PriceProfrole> findByProfroleId(Long profroleId) {
        String jpql = "SELECT p FROM PriceProfrole p JOIN p.profroles pr WHERE pr.id = :profroleId";
        TypedQuery<PriceProfrole> query = entityManager.createQuery(jpql, PriceProfrole.class);
        query.setParameter("profroleId", profroleId);
        return query.getResultList();
    }
}


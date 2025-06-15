package com.example.backend.DAO;

import com.example.backend.models.PriceProfrole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Collection;
import java.util.Collections;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PriceProfroleDao {

  @PersistenceContext
  private EntityManager entityManager;

  public List<Long> findByProfroleId(Collection<Long> profroleIds) {
    if (profroleIds == null || profroleIds.isEmpty()) {
      return Collections.emptyList();
    }

    String jpql = "SELECT DISTINCT p.id FROM PriceProfrole p JOIN p.profroles pr WHERE pr.id IN :profroleIds";
    TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
    query.setParameter("profroleIds", profroleIds);
    return query.getResultList();
  }


  public String findProfroleNameById(Long profroleId) {
    try {
      return entityManager.createQuery(
              "select p.name from PriceProfrole p where p.id = :profroleId", String.class)
          .setParameter("profroleId", profroleId)
          .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}


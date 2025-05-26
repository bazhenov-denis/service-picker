package com.example.backend.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PriceRegionDao {

  @PersistenceContext
  private EntityManager entityManager;


  public List<Long> findRegionIdsByAreaIds(Collection<Long> areaIds) {
    if (areaIds == null || areaIds.isEmpty()) {
      return Collections.emptyList();
    }
    return entityManager.createQuery(
            "select distinct r.id " +
                "from PriceRegion r " +
                " join r.areas a " +
                "where a.id in :areaIds", Long.class
        )
        .setParameter("areaIds", areaIds)
        .getResultList();
  }

  public String findRegionNameById(Long regionId) {
    return entityManager.createQuery(
            "select r.name from PriceRegion r where r.id = :regionId", String.class)
        .setParameter("regionId", regionId)
        .getSingleResult();
  }

}

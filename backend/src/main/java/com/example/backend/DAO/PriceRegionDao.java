package com.example.backend.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PriceRegionDao {

  @PersistenceContext
  private EntityManager entityManager;


  public List<Long> findAreaIdsByRegionIds(Collection<Long> regionIds) {
    if (regionIds == null || regionIds.isEmpty()) {
      return List.of();
    }
    List<Long> ids = entityManager.createQuery(
            "select distinct a.id from PriceRegion r join r.areas a where r.id in :regionIds", Long.class)
        .setParameter("regionIds", regionIds)
        .getResultList();
    return ids;
  }
}

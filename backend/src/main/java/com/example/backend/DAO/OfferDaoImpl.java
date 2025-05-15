package com.example.backend.DAO;

import com.example.backend.models.Offer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OfferDaoImpl implements OfferDao {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Offer> getByRegionAndProfroleGroup(Long regionId, Integer profroleGroupId) {
    TypedQuery<Offer> query = entityManager.createQuery(
        "SELECT e FROM Offer e WHERE e.regionId=:regionId AND e.profroleGroupId=:profroleGroupId",
        Offer.class);
    query.setParameter("regionId", regionId);
    query.setParameter("profroleGroupId", profroleGroupId);
    return query.getResultList();
  }

  @Override
  @Transactional
  public void save(Offer offer) {
    entityManager.persist(offer);
  }
}

package com.example.backend.DAO;

import com.example.backend.models.Offer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class OfferDaoImpl implements OfferDao {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Offer> getByRegionAndProfroleGroup(
      Collection<Long> regionIds,
      Collection<Long> profroleGroupIds
  ) {

    String jpql = "SELECT e FROM Offer e WHERE 1=1";
    if (regionIds != null && !regionIds.isEmpty()) {
      jpql += " AND e.regionId IN :regionIds";
    }
    if (profroleGroupIds != null && !profroleGroupIds.isEmpty()) {
      jpql += " AND e.profroleGroupId IN :profroleGroupIds";
    }

    TypedQuery<Offer> query = entityManager.createQuery(jpql, Offer.class);

    if (regionIds != null && !regionIds.isEmpty()) {
      query.setParameter("regionIds", regionIds);
    }
    if (profroleGroupIds != null && !profroleGroupIds.isEmpty()) {
      query.setParameter("profroleGroupIds", profroleGroupIds);
    }

    return query.getResultList();
  }

  @Override
  @Transactional
  public void save(Offer offer) {
    entityManager.persist(offer);
  }


  public List<Offer> findOffers(String sql, Map<String, Object> params) {
    Query query = entityManager.createNativeQuery(sql, Offer.class);
    params.forEach(query::setParameter);
    @SuppressWarnings("unchecked")
    List<Offer> offers = query.getResultList();
    return offers;
  }
}

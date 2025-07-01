package com.example.backend.DAO;

import com.example.backend.models.Offer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class OfferDaoImpl implements OfferDao {

  @PersistenceContext
  private EntityManager entityManager;

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

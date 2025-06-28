package com.example.backend.DAO;

import com.example.backend.models.OptionScore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class OptionScoreDao {
  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void save(OptionScore optionScore) {
    entityManager.persist(optionScore);
  }
}

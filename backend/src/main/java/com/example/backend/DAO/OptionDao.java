package com.example.backend.DAO;

import com.example.backend.models.Option;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class OptionDao {
  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void save(Option option) {
    entityManager.persist(option);
  }
}

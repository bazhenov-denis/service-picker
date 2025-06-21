// com/example/backend/DAO/ScoreTypeDao.java
package com.example.backend.DAO;

import com.example.backend.models.ScoreType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ScoreTypeDao {

  @PersistenceContext
  private EntityManager em;

  @Transactional(readOnly = true)
  public List<ScoreType> findAll() {
    TypedQuery<ScoreType> q = em.createQuery(
        "SELECT st FROM ScoreType st ORDER BY st.id",
        ScoreType.class
    );
    return q.getResultList();
  }
}

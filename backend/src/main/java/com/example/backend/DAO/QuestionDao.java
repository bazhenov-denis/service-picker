package com.example.backend.DAO;

import com.example.backend.models.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao{

  @PersistenceContext
  private EntityManager entityManager;


  public List<Question> findAllWithOptions() {
    TypedQuery<Question> query = entityManager.createQuery(
        "select q " +
            "from Question q " +
            "  left join fetch q.options " +
            "order by q.id asc",
        Question.class
    );
    return query.getResultList();
  }
}
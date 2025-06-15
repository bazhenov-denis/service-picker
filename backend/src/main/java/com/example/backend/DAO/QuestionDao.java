package com.example.backend.DAO;

import com.example.backend.models.OptionScore;
import com.example.backend.models.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
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

  public List<OptionScore> findScoresByOptionIds(List<Long> optionIds) {
    String jpql = "SELECT os " +
        "FROM OptionScore os " +
        "JOIN FETCH os.scoreType " +
        "WHERE os.option.id IN :ids";
    return entityManager.createQuery(jpql, OptionScore.class)
        .setParameter("ids", optionIds)
        .getResultList();
  }

  public List<String> findTypesByQuestionIds(List<Long> ids) {
    String jpql = "SELECT q.type FROM Question q WHERE q.id IN :ids";
    return entityManager.createQuery(jpql, String.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  public Optional<String> findReferenceTypeByQuestionId(Long id) {
    String jpql = "SELECT q.referenceType FROM Question q WHERE q.id = :id";
    List<String> result = entityManager.createQuery(jpql, String.class)
        .setParameter("id", id)
        .getResultList();

    return result.stream().findFirst();
  }
}
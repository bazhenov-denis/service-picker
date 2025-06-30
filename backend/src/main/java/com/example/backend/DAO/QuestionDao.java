package com.example.backend.DAO;

import com.example.backend.models.OptionScore;
import com.example.backend.models.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDao{

  @PersistenceContext
  private EntityManager entityManager;

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

  public List<Question> findAllWithOptions() {
    return entityManager.createQuery("""
        SELECT q
        FROM Question q
        LEFT JOIN FETCH q.options o
        ORDER BY q.id ASC
      """, Question.class)
        .getResultList();
  }

  public List<Question> findActiveWithOptions() {
    return entityManager.createQuery("""
        SELECT q
        FROM Question q
        LEFT JOIN FETCH q.options o
        WHERE q.active = TRUE
        ORDER BY q.id ASC
      """, Question.class)
        .getResultList();
  }

  public List<OptionScore> findScoresByOptionIds(List<Long> optionIds) {
    return entityManager.createQuery("""
        SELECT os
        FROM OptionScore os
        JOIN FETCH os.scoreType st
        WHERE os.option.id IN :ids
      """, OptionScore.class)
        .setParameter("ids", optionIds)
        .getResultList();
  }

  public Integer getNextPosition() {
    return entityManager.createQuery("""
        SELECT max(q.position)
        FROM Question q
        """, Integer.class)
        .getSingleResult() + 1;
  }

  public Long getMaxId() {
    return entityManager.createQuery("""
        SELECT max(q.id)
        FROM Question q
        """, Long.class)
        .getSingleResult();
  }

  public List<Question> getQuestionsByIds(Collection<Long> ids) {
    return entityManager.createQuery("""
        SELECT q
        FROM Question q
        WHERE q.id IN :ids
        """, Question.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  public Question getQuestionById(Long id) {
    return entityManager.createQuery("""
        SELECT q
        FROM Question q
        WHERE q.id = :id
        """, Question.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  @Transactional
  public void save(Question question) {
    entityManager.persist(question);
  }

  @Transactional
  public void saveMultiple(List<Question> questions) {
    for (Question question : questions) {
      entityManager.persist(question);
    }
  }

  public Set<Integer> getPositionsByIds(Set<Long> ids) {
    return entityManager.createQuery("""
        SELECT q.position
        FROM Question q
        WHERE q.id IN :ids
        """, Integer.class)
        .setParameter("ids", ids)
        .getResultStream()
        .collect(Collectors.toSet());
  }
}
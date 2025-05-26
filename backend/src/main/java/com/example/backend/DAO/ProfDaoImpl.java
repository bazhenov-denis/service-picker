package com.example.backend.DAO;

import com.example.backend.models.ProfroleMap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class ProfroleMapDaoImpl implements ProfroleMapDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer getPriceProfroleGroupIdByProfessionalRoleId(Integer professionalRoleId) {
        TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT m.id.priceProfroleGroupId FROM ProfroleMap m WHERE m.id.professionalRoleId = :profroleId",
                Integer.class
        );
        query.setParameter("profroleId", professionalRoleId);
        return query.getSingleResult();
    }
}

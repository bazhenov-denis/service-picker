package com.example.backend;

import com.example.backend.models.PriceProfrole;
import com.example.backend.models.Profrole;
import com.example.backend.services.PriceProfroleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false"
})
@Transactional
public class PriceProfroleServiceTest {

    @Autowired
    private PriceProfroleService priceProfroleService;

    @Autowired
    private EntityManager entityManager;

    private Long profroleId;

    @BeforeEach
    public void setUp() {
        Profrole profrole = new Profrole();
        profrole.setName("QA");
        entityManager.persist(profrole);

        PriceProfrole group = new PriceProfrole();
        group.setName("Testers Price Group");
        group.setProfroles(Set.of(profrole));
        entityManager.persist(group);
        PriceProfrole group2 = new PriceProfrole();
        group2.setName("IT Price Group");
        group2.setProfroles(Set.of(profrole));
        entityManager.persist(group2);

        entityManager.flush();

        this.profroleId = profrole.getId();
    }

    @Test
    public void testGetGroupsByProfroleId() {
        List<PriceProfrole> result = priceProfroleService.getPriceGroupsByProfroleId(profroleId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Testers Price Group");
        assertThat(result.get(1).getName()).isEqualTo("IT Price Group");
    }

    @Test
    public void testNotExistingGroupsByProfroleId() {
        long differentProfroleId = profroleId + 100L;
        List<PriceProfrole> result = priceProfroleService.getPriceGroupsByProfroleId(differentProfroleId);
        assertThat(result).hasSize(0);
    }
}


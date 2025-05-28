package com.example.backend.services;

import com.example.backend.DAO.PriceProfroleDao;
import com.example.backend.models.PriceProfrole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PriceProfroleService {

    private final PriceProfroleDao priceProfroleDao;

    public PriceProfroleService(PriceProfroleDao priceProfroleDao) {
        this.priceProfroleDao = priceProfroleDao;
    }

    @Transactional(readOnly = true)
    public List<PriceProfrole> getPriceGroupsByProfroleId(Long profroleId) {
        return priceProfroleDao.findByProfroleId(profroleId);
    }
}


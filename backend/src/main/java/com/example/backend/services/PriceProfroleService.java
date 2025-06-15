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

  public String getProfroleNameById(Long profroleId) {
    String name = priceProfroleDao.findProfroleNameById(profroleId);

    if (name == null) {
      return "Все";
    }
    return name;
  }

  @Transactional(readOnly = true)
  public List<Long> getPriceGroupsByProfroleId(List<Long> profroleId) {
    return priceProfroleDao.findByProfroleId(profroleId);
  }
}


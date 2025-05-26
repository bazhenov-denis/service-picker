package com.example.backend.services;

import com.example.backend.DAO.PriceRegionDao;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PriceRegionService {

  private final PriceRegionDao regionDao;

  public PriceRegionService(PriceRegionDao regionDao) {
    this.regionDao = regionDao;
  }

  public String getRegionNameById(Long regionId) {
    return regionDao.findRegionNameById(regionId);
  }

  public List<Long> getAreaIdsByRegionIds(Collection<Long> priceRegionIds) {
    return regionDao.findRegionIdsByAreaIds(priceRegionIds);
  }
}

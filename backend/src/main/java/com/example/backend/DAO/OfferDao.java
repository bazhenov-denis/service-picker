package com.example.backend.DAO;

import com.example.backend.models.Offer;
import java.util.List;

public interface OfferDao {
  List<Offer> getByRegionAndProfroleGroup(Long regionId, Integer profroleGroupId);
  void save(Offer offer);
}

package com.example.backend.DAO;

import com.example.backend.models.Offer;
import java.util.Collection;
import java.util.List;

public interface OfferDao {
  List<Offer> getByRegionAndProfroleGroup(
      Collection<Long> regionIds,
      Collection<Long> profroleGroupIds
  );

  void save(Offer offer);
}

package com.example.backend.services;

import com.example.backend.DTO.ClaimDto;
import com.example.backend.DTO.OfferListDto;
import org.springframework.stereotype.Service;

@Service
public interface OfferService {

  OfferListDto pick(ClaimDto claim);
}

package com.example.backend.handlers;

import com.example.backend.DTO.ProcessingResult;
import com.example.backend.query.QueryBuilder;

public interface CriteriaHandler {
  void apply(ProcessingResult result, QueryBuilder queryBuilder);
}

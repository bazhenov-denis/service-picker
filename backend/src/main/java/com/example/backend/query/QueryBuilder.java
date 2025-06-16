package com.example.backend.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
  private final List<String> whereClauses = new ArrayList<>();
  private final Map<String, Object> params = new HashMap<>();

  public void addCondition(String clause, String paramName, Object value) {
    whereClauses.add(clause);
    params.put(paramName, value);
  }

  public String buildSql() {
    String where = whereClauses.isEmpty()
        ? ""
        : " WHERE " + String.join(" AND ", whereClauses);
    return "SELECT * FROM offers" + where;
  }

  public Map<String, Object> getParams() {
    return params;
  }
}
package com.example.backend.DAO;

import com.example.backend.models.Question;
import java.util.List;

public interface QuestionDao {
  List<Question> findAllWithOptions();
}

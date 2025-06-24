package com.example.backend.services;

import com.example.backend.DAO.ScoreTypeDao;
import com.example.backend.DTO.ScoreTypeDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ScoreTypeService {

  private final ScoreTypeDao dao;

  public ScoreTypeService(ScoreTypeDao dao) {
    this.dao = dao;
  }

  public List<ScoreTypeDTO> getAllScoreTypes() {
    return dao.findAll().stream()
        .map(st -> new ScoreTypeDTO(
            st.getId(),
            st.getCode(),
            st.getTitle()
        ))
        .toList();
  }
}

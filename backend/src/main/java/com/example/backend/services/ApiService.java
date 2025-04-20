package com.example.backend.services;

import com.example.backend.DTO.*;
import com.example.backend.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ApiService {

  private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
  private static final int MAX_RETRIES = 3;
  private static final int RETRY_DELAY_MILLIS = 1000;

  private final String baseUrl;
  private final RestClient restClient;
  private final ObjectMapper objectMapper;

  public ApiService(
      RestClient restClient, ObjectMapper objectMapper,
      @Value("${api.hh.base_url:https://api.hh.ru}") String baseUrl
  ) {
    this.restClient = restClient;
    this.objectMapper = objectMapper;
    this.baseUrl = baseUrl;
  }

  @Retryable(
      value = {RestClientException.class},
      maxAttempts = MAX_RETRIES,
      backoff = @Backoff(delay = RETRY_DELAY_MILLIS))
  public VacancyResult getVacancyCount(Integer area, Integer role) {
    String uri = UriComponentsBuilder.fromUriString(baseUrl + "/vacancies")
        .queryParam("area", area)
        .queryParam("professional_role", role)
        .toUriString();

    try {
      String json = executeGetRequest(uri);
      JsonNode root = objectMapper.readTree(json);
      int count = root.path("found").asInt();
      return new VacancyResult(true, "OK", count);
    } catch (Exception e) {
      logger.error("Ошибка при выполнении запроса вакансий", e);
      throw new RestClientException("Ошибка API: " + e.getMessage(), e);
    }
  }

  @Recover
  public VacancyResult recoverVacancyCount(RestClientException e, Integer area, Integer role) {
    logger.error("Не удалось получить количество вакансий (area={}, role={}): {}", area, role, e.getMessage());
    return new VacancyResult(false, "Ошибка API после повторных попыток: " + e.getMessage(), 0);
  }


  @Retryable(
      value = {RestClientException.class},
      maxAttempts = MAX_RETRIES,
      backoff = @Backoff(delay = RETRY_DELAY_MILLIS)
  )
  public List<AreaDTO> getAreas() {
    String uri = baseUrl + "/areas";
    try {
      String json = executeGetRequest(uri);
      return objectMapper.readValue(
          json, new TypeReference<>() {
          }
      );
    } catch (Exception e) {
      logger.error("Ошибка при получении регионов", e);
      throw new RestClientException("Ошибка API (areas): " + e.getMessage(), e);
    }
  }

  @Recover
  public List<AreaDTO> recoverAreas(RestClientException e) {
    logger.error("Не удалось получить регионы после повторных попыток: {}", e.getMessage());
    throw new ApiException("Ошибка API (areas) после повторных попыток: " + e.getMessage(), e);
  }

  @Retryable(
      value = {RestClientException.class},
      maxAttempts = MAX_RETRIES,
      backoff = @Backoff(delay = RETRY_DELAY_MILLIS)
  )
  public ProfessionalRolesResponseDTO getProfessionalRolesDictionary() {
    String uri = baseUrl + "/professional_roles";
    try {
      String json = executeGetRequest(uri);
      return objectMapper.readValue(json, ProfessionalRolesResponseDTO.class);
    } catch (Exception e) {
      logger.error("Ошибка при получении профессиональных ролей", e);
      throw new RestClientException("Ошибка API (professional_roles): " + e.getMessage(), e);
    }
  }

  @Recover
  public ProfessionalRolesResponseDTO recoverProfessionalRolesDictionary(RestClientException e) {
    logger.error("Не удалось получить словарь профессиональных ролей после повторных попыток: {}", e.getMessage());
    throw new ApiException("Ошибка API (professional_roles) после повторных попыток: " + e.getMessage(), e);
  }


  private String executeGetRequest(String uri) {
    return restClient.get()
        .uri(uri)
        .retrieve()
        .onStatus(
            HttpStatusCode::isError, (req, res) -> {
              throw new RestClientException("Ошибка от API: " + res.getStatusCode());
            }
        )
        .body(String.class);
  }
}

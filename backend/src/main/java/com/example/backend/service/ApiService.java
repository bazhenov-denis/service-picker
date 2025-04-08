package com.example.backend.service;

import com.example.backend.DTO.VacancyResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ApiService {

    private static final String BASE_URL = "https://api.hh.ru";
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MILLIS = 1000;
    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VacancyResult getVacancyCount(Integer area, Integer role) {
        String uri = UriComponentsBuilder.fromUriString(BASE_URL + "/vacancies")
                .queryParam("area", area)
                .queryParam("professional_role", role)
                .toUriString();


        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                String json = restClient.get()
                        .uri(uri)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, (req, res) -> {
                            throw new RestClientException("Ошибка от HH API: " + res.getStatusCode());
                        })
                        .body(String.class);

                JsonNode root = objectMapper.readTree(json);
                int count = root.path("found").asInt();
                return new VacancyResult(true, "OK", count);

            } catch (Exception e) {
                if (attempt == MAX_RETRIES) {
                    return new VacancyResult(false, "Ошибка API: " + e.getMessage(), 0);
                }

                try {
                    Thread.sleep(RETRY_DELAY_MILLIS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return new VacancyResult(false, "Прервано", 0);
                }
            }
        }
        return new VacancyResult(false, "Неизвестная ошибка", 0);
    }
}

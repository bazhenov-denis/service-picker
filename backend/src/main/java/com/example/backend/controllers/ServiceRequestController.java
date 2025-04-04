package com.example.backend.controllers;

import com.example.backend.models.ResumesAccessService;
import com.example.backend.models.ServiceRequest;
import com.example.backend.models.ServiceResponse;
import com.example.backend.models.VacancyService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceRequestController {
  @PostMapping("/service-request")
  public ServiceResponse requestService(@RequestBody ServiceRequest request) {
    Map<Integer, VacancyService> vacancyServices = new HashMap<>();
    Map<Integer, ResumesAccessService> resumesAccessServices = new HashMap<>();

    vacancyServices.put(1, new VacancyService(
        request.area(),
        request.profession(),
        50,
        "Regular",
        30,
        100.0,
        5000.0
        ));
    resumesAccessServices.put(1, new ResumesAccessService(
        request.area(),
        request.profession(),
        30,
        100,
        4500.0
        ));

    return new ServiceResponse(vacancyServices, resumesAccessServices);
    // return new ServiceResponse(vacancyServices, Collections.emptyMap());
  }
}

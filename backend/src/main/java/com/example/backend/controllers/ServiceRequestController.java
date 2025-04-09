package com.example.backend.controllers;

import com.example.backend.DTO.ResumesAccessServiceDTO;
import com.example.backend.DTO.ServiceRequestControllerRequest;
import com.example.backend.DTO.ServiceRequestControllerResponse;
import com.example.backend.DTO.VacancyServiceDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceRequestController {
  @PostMapping("/service-request")
  public ResponseEntity<ServiceRequestControllerResponse> requestService(@RequestBody ServiceRequestControllerRequest request) {

    Map<Integer, VacancyServiceDTO> vacancyServices = new HashMap<>();
    Map<Integer, ResumesAccessServiceDTO> resumesAccessServices = new HashMap<>();

    vacancyServices.put(1, new VacancyServiceDTO(
        request.areaId(),
        request.professionId(),
        50,
        "Regular",
        30,
        100.0,
        5000.0
        ));
    resumesAccessServices.put(1, new ResumesAccessServiceDTO(
        request.areaId(),
        request.professionId(),
        30,
        100,
        4500.0
        ));

    return ResponseEntity.ok(new ServiceRequestControllerResponse(vacancyServices, resumesAccessServices));
  }
}

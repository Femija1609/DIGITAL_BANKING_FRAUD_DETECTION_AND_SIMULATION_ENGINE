package org.example.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MLClientService {

    private static final String ML_URL = "http://localhost:5000/predict";

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> predictFraud(Map<String, Object> features) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(features, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(ML_URL, request, Map.class);

        return response.getBody();
    }
}

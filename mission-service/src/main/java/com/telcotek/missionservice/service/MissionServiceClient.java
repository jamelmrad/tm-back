package com.telcotek.missionservice.service;

import com.telcotek.missionservice.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MissionServiceClient {

    private final RestTemplate restTemplate;

    @Autowired
    public MissionServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getJwtToken(String username, String password) {
        AuthRequest authRequest = new AuthRequest(username, password);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://authentication-service-url/login",
                authRequest,
                Map.class
        );

        System.out.println(response);

        Map<String, String> responseBody = response.getBody();
        return responseBody.get("token"); // maybe it's bezcoder in our example
    }
}



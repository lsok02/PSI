package org.example.flightservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PassengerClient {

    private final RestTemplate restTemplate;

    public PassengerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHelloFromPassengerService() {
        return restTemplate.getForObject(
                "http://localhost:8082/passengers/hello",
                String.class
        );
    }
}
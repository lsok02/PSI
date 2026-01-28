package org.example.flightservice.controller;

import org.example.flightservice.client.PassengerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightController {

    private final PassengerClient passengerClient;

    public FlightController(PassengerClient passengerClient) {
        this.passengerClient = passengerClient;
    }

    @GetMapping("/flights/hello")
    public String hello() {
        String response = passengerClient.getHelloFromPassengerService();
        return "Hello from Flight Service ✈️ | Passenger says: " + response;
    }
}
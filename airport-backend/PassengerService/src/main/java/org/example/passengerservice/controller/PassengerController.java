package org.example.passengerservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PassengerController {

    @GetMapping("/passengers/hello")
    public String helloFromPassengerService() {
        return "Hello from Passenger Service 👋";
    }
}
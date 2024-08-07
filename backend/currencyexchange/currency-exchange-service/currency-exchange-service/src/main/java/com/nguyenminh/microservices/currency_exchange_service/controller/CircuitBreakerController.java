package com.nguyenminh.microservices.currency_exchange_service.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CircuitBreakerController {
    @GetMapping("/sample-test")

    public String testSth(){
        return "Hello";
    }
}

package com.biy.social.traffic_sim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sim")
public class TrafficController {
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public ResponseEntity<String> getCurrentSimulation(@RequestHeader(value = "traceId") String traceId){
        return ResponseEntity.ok("Everything is working");
    }
}

package com.biy.social.traffic_sim.controller;

import com.biy.social.traffic_sim.model.Simulation;
import com.biy.social.traffic_sim.service.SimulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sim")
@Slf4j
public class TrafficController {
    @Autowired
    SimulationService simulationService;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public ResponseEntity<Simulation> getCurrentSimulation(@RequestHeader(value = "traceId") String traceId){
        log.info("Getting current simulation");
        return ResponseEntity.ok(simulationService.getCurrentSimulation());
    }

    @PostMapping
    public ResponseEntity<Simulation> createSimulation(@RequestHeader(value = "traceId") String traceId,
                                                       @RequestHeader(value = "users") int users,
                                                       @RequestHeader(value = "duration") long duration){

        Simulation simulation = new Simulation(users, duration);
        log.info("Creating simulation: {}", simulation);
        return ResponseEntity.ok(simulationService.startSimulation(simulation));
    }
}

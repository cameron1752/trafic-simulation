package com.biy.social.traffic_sim.service;

import com.biy.social.traffic_sim.exception.SimulationException;
import com.biy.social.traffic_sim.model.Feed;
import com.biy.social.traffic_sim.model.Simulation;
import com.biy.social.traffic_sim.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class SimulationService {
    Simulation simulation;
    private final static double lat = 39.9526;
    private final static double lon = -75.1652;

    @Autowired
    APIService apiService;

    public Simulation startSimulation(Simulation simulation){

        if (this.simulation != null &&this.simulation.getStatus().equals(Simulation.Status.RUNNING)){
            throw new SimulationException("Simulation already running!");
        }

        log.info("Starting simulation: {}", simulation);
        this.simulation = simulation;

        // fetch users from table
        List<User> users = apiService.getUsers();
        log.info("Found {} users", users.size());

        int threads = 0;

        // if there are more users than requested use requested
        if (users.size() >= simulation.getUsers()){
            threads = simulation.getUsers();
        } else {
            // otherwise use the amount of users we had
            threads = users.size();
        }

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        this.simulation.setStatus(Simulation.Status.RUNNING);

        // 1 thread per user
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++){
            User user = users.get(i);
            futures.add(CompletableFuture.runAsync(() -> simulateUser(user)));
        }
            // each user runs random interactions on their feed in order
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((result, error) -> {
                    simulation.setStatus(error == null
                            ? Simulation.Status.COMPLETED
                            : Simulation.Status.FAILED);
                    executor.shutdown();                    // release the threads
                });

        return simulation;
    }

    public void simulateUser(User user){
        log.info("Simulating user: {}", user);
        // fetch feed for user
        try {
            log.info("Fetching feed for user: {}", user.getUser_id());
            Feed feed = apiService.getFeed(user.getUser_id(), lat, lon);
            log.info("feed length: {}", feed.getFeedObjects().size());

            // for each feed object ->
            // random selection of interaction path on each video

            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Simulation Completed for user: {}", user);
    }

    public Simulation getCurrentSimulation(){
        log.info("Fetching simulation: {}", this.simulation);
        return this.simulation;
    }

}

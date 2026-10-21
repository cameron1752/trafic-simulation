package com.biy.social.traffic_sim.service;

import com.biy.social.traffic_sim.exception.SimulationException;
import com.biy.social.traffic_sim.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    InteractionService interactionService;

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
            for (FeedObject object : feed.getFeedObjects()){
                List<VideoInteractionEventType> interactions = interactionService.getRandomPath();
                log.info("Video [{}] has interactions [{}]", object.getVideo().getId(), interactions);
                performInteraction(user, object, interactions);
            }
            // random selection of interaction path on each video

            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Simulation Completed for user: {}", user);
    }

    private void performInteraction(User user, FeedObject object, List<VideoInteractionEventType> interactions) {
        Video video = object.getVideo();

        for (VideoInteractionEventType interaction : interactions){
            // call the API for this interaction & video w/ random time
            long sleep = (long) (Math.random() * 1000);
            log.info("Sending [{}] interaction for user [{}]", interaction, user.getUser_id());

            VideoInteraction interactionEvent = new VideoInteraction();

            interactionEvent.setUserId(user.getUser_id());
            interactionEvent.setVideoId(video.getId());
            interactionEvent.setEventType(interaction);
            interactionEvent.setWatchDuration(null);
            interactionEvent.setVideoDuration(60L);
            interactionEvent.setCreatedAt(LocalDateTime.now());
            interactionEvent.setLatitude(39.976755);
            interactionEvent.setLongitude(-75.190711);

            apiService.sendInteraction(interactionEvent);

            log.info("[{}] interaction [{}] sleeping [{}]", video.getId(), interaction, sleep);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Simulation getCurrentSimulation(){
        log.info("Fetching simulation: {}", this.simulation);
        return this.simulation;
    }

}

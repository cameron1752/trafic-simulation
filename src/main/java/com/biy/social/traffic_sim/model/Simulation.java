package com.biy.social.traffic_sim.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Simulation {
    UUID simulationId;
    int users;
    long duration;
    private volatile Status status = Status.NEW;

    public enum Status { NEW, RUNNING, COMPLETED, FAILED }

    public Simulation(int users, long duration){
        this.simulationId = UUID.randomUUID();
        this.users = users;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Simulation{" +
                "simulationId=" + simulationId +
                ", users=" + users +
                ", duration=" + duration +
                '}';
    }
}

package com.biy.social.traffic_sim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedObject {
    private Video video;
    private boolean isLiked;
    private double score;
    private double distance;
    private int age;
    private List<VideoInteraction> videoInteractionList;
    private double completionRate;
    private double watchRate;
    private double engagementRate;

    public FeedObject(Video video, boolean isLiked){
        this.video = video;
        this.isLiked = isLiked;
    }

    public int getAge(){
        return (int) Duration.between(video.getCreatedAt(), OffsetDateTime.now()).toDays();
    }
}

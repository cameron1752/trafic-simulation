package com.biy.social.traffic_sim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    private UUID id;
    private User userId;
    private String videoUrl;
    private String thumbnailUrl;
    private Double latitude;
    private Double longitude;
    private Long locationId;
    private String caption;
    private OffsetDateTime createdAt;
    private Visibility visibility;
    private long likes;
    private long comments;

    public Video(UUID id){
        this.id = id;
    }
}
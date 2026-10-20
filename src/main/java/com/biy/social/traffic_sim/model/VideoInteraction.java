package com.biy.social.traffic_sim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInteraction {

    private Long id;
    private Long userId;
    private UUID videoId;
    private VideoInteractionEventType eventType;
    private Long watchDuration;
    private Long videoDuration;
    private LocalDateTime createdAt;
    private Double latitude;
    private Double longitude;

}
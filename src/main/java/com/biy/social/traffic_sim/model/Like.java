package com.biy.social.traffic_sim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    private UUID videoId;
    private Long userId;
    private LocalDateTime createdAt;

}
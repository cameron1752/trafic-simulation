package com.biy.social.traffic_sim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private Long id;
    private UUID videoId;
    private User userId;
    private String text;
    private LocalDateTime createdAt;
}
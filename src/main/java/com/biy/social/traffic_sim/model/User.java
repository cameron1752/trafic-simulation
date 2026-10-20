package com.biy.social.traffic_sim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long user_id;
    private String providerId;
    private String username;
    private String email;
    private String name;
    private String avatar_url;
    private LocalDateTime created_at;

}

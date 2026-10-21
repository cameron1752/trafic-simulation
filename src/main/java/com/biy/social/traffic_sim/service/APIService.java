package com.biy.social.traffic_sim.service;

import com.biy.social.traffic_sim.model.Feed;
import com.biy.social.traffic_sim.model.User;
import com.biy.social.traffic_sim.model.VideoInteraction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
@Slf4j
public class APIService {
    private final RestClient restClient;
    private static final String baseUrl = "http://localhost:8080/v1";
    public APIService() {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<User> getUsers() {
        try {
            return restClient.get()
                    .uri("/users/all")
                    .header("traceId", MDC.get("traceId"))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<User>>() {});
        } catch (RestClientResponseException e) {
            log.error("API call failed: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;                                          // or map to your own exception
        }
    }

    public Feed getFeed(long userId, double lat, double lon){
        try {
            return restClient.get()
                    .uri("/feed/" + userId)
                    .header("traceId", MDC.get("traceId"))
                    .header("lat", String.valueOf(lat))
                    .header("long", String.valueOf(lon))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(Feed.class);
        } catch (RestClientResponseException e) {
            log.error("API call failed: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;                                          // or map to your own exception
        }
    }

    public void sendInteraction(VideoInteraction interaction){
        try {
            restClient.post()
                    .uri("/interactions")
                    .header("traceId", MDC.get("traceId"))
                    .body(interaction)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            log.error("API call failed: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;                                          // or map to your own exception
        }
    }
}

package com.biy.social.traffic_sim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Feed {
    int count = 0;
    List<FeedObject> feedObjects = new ArrayList<>();

    public void add(FeedObject feedObject){
        feedObjects.add(feedObject);
        count++;
    }

    public Feed sort(){
        log.info("we are sorting the feedObjects list {}", this.feedObjects);

        feedObjects.sort(Comparator.comparing(FeedObject::getScore).reversed());
        count = feedObjects.size();

        return this;
    }
}

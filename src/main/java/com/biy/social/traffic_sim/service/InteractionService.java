package com.biy.social.traffic_sim.service;

import com.biy.social.traffic_sim.model.VideoInteraction;
import com.biy.social.traffic_sim.model.VideoInteractionEventType;
import org.springframework.stereotype.Service;
import static com.biy.social.traffic_sim.model.VideoInteractionEventType.*;
import java.util.List;

@Service
public class InteractionService {

    public int[] distribution = new int[100];
    private int cursor = 0;

    public InteractionService(){
        fill(35, 0, 1, 2);              // impression / skip      -> 35%
        fill(30, 3, 4, 5);              // partial watch          -> 30%
        fill(15, 6);                    // full watch             -> 15%
        fill(10, 7, 8, 9, 10);          // watch with like        -> 10%
        fill(6,  11, 12);               // watch with comment     ->  6%
        fill(3,  13, 14, 15);           // watch with share       ->  3%
        fill(1,  16);                   // strong intent          ->  1%
    }

    public static final List<List<VideoInteractionEventType>> PATHS = List.of(
            // impression / skip ->
            List.of(IMPRESSION),
            List.of(IMPRESSION, SKIP),
            List.of(IMPRESSION, PLAY, SKIP),

            // partial watch ->
            List.of(IMPRESSION, PLAY, WATCH_25, SKIP),
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, SKIP),
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, SKIP),

            // full watch, no action ->
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE),

            // watch with like ->
            List.of(IMPRESSION, PLAY, LIKE, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE),
            List.of(IMPRESSION, PLAY, WATCH_25, LIKE, SKIP),
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, LIKE),
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE, LIKE),

            // watch with comment ->
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE, COMMENT),
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE, LIKE, COMMENT),

            // watch with share ->
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE, SHARE),
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE, LIKE, SHARE),
            List.of(IMPRESSION, PLAY, WATCH_25, SHARE, SKIP),

            // strong intent / high value ->
            List.of(IMPRESSION, PLAY, WATCH_25, WATCH_50, WATCH_75, WATCH_COMPLETE, LIKE, COMMENT, SHARE)
    );

    private void fill(int percent, int... pathIndexes) {
        for (int i = 0; i < percent; i++) {
            distribution[cursor++] = pathIndexes[i % pathIndexes.length];
        }
    }

    public List<VideoInteractionEventType> getRandomPath(){
        // impression / skip    -> 35%
        // partial watch        -> 30%
        // full watch           -> 15%
        // full watch + like    -> 10%
        // full watch + comment -> 6%
        // full watch + share   -> 3%
        // full watch + inter   -> 1%

        int number = (int) (Math.random() * 100);

        return PATHS.get(distribution[number]);
    }

}

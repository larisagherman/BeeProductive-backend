package com.beeproductive.backend.controller;

import com.beeproductive.backend.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Debug controller for development and testing purposes.
 * Only active when the 'dev' profile is enabled.
 * NEVER enable this in production!
 */
@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
@Profile("dev") // Only active in dev profile
public class DebugController {

    private final ChallengeService challengeService;

    /**
     * Creates sample challenges for testing.
     * Endpoint: POST /debug/create-sample-challenges
     *
     * @return Map with created challenge IDs
     */
    @PostMapping("/create-sample-challenges")
    public Map<String, Object> createSampleChallenges() {
        List<Long> createdIds = challengeService.createSampleChallenges();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Successfully created " + createdIds.size() + " sample challenges");
        response.put("challengeIds", createdIds);
        response.put("count", createdIds.size());

        return response;
    }

    /**
     * Health check endpoint to verify debug mode is active
     */
    @GetMapping("/status")
    public Map<String, String> debugStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "Debug mode is ACTIVE");
        status.put("warning", "This should NEVER be active in production!");
        return status;
    }
}


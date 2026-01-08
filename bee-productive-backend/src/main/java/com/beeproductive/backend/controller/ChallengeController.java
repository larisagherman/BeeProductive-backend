package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.ChallengeRequestDto;
import com.beeproductive.backend.dto.ChallengeResponseDto;
import com.beeproductive.backend.entity.Challenge;
import com.beeproductive.backend.service.ChallengeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    @PostMapping
    public void create(@RequestBody ChallengeRequestDto challengeRequestDto) {
        challengeService.createChallenge(challengeRequestDto);
    }
    @GetMapping
    public List<ChallengeResponseDto> getAllChallenges() {
        return challengeService.getAllChallenges();
    }

    @GetMapping("/{id}")
    public ChallengeResponseDto getChallengeById(@PathVariable("id") Long id) {
        return challengeService.getChallengeById(id);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        challengeService.deleteChallenge(id);
    }
    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody ChallengeRequestDto challengeRequestDto) {
        challengeService.updateChallenge(id, challengeRequestDto);
    }
}

package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.*;
import com.beeproductive.backend.security.FirebaseUserPrincipal;
import com.beeproductive.backend.service.ChallengeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    private ChallengeService challengeService;

    @GetMapping
    public List<ChallengeDto> getAllChallenges() {
        return challengeService.getAllChallenges();
    }

    @GetMapping("/{challengeId}")
    public ChallengeDto getChallengeById(@PathVariable Long challengeId) {
        return challengeService.getChallengeById(challengeId);
    }

    @PostMapping("/join")
    public JoinChallengeResponseDto joinChallenge(
            @RequestBody JoinChallengeRequestDto requestDto,
            @AuthenticationPrincipal FirebaseUserPrincipal principal) {
        // Set the authenticated user's UID in the DTO
        requestDto.setUserUid(principal.getUid());
        return challengeService.joinChallenge(requestDto);
    }

    @GetMapping("/my-challenges")
    public List<UserChallengeEnrollmentDto> getMyEnrolledChallenges(
            @AuthenticationPrincipal FirebaseUserPrincipal principal) {
        return challengeService.getMyEnrolledChallenges(principal.getUid());
    }

    @PutMapping("/update-status")
    public UpdateChallengeStatusResponseDto updateChallengeStatus(
            @RequestBody UpdateChallengeStatusRequestDto requestDto,
            @AuthenticationPrincipal FirebaseUserPrincipal principal) {
        // Set the authenticated user's UID in the DTO
        requestDto.setUserUid(principal.getUid());
        return challengeService.updateChallengeStatus(requestDto);
    }
}

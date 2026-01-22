package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.UpdateEnrollmentStatusRequestDto;
import com.beeproductive.backend.dto.UserChallengeEnrollmentResponseDto;
import com.beeproductive.backend.service.UserChallengeEnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserChallengeEnrollmentController {

    private final UserChallengeEnrollmentService enrollmentService;

    @PostMapping("/{userId}/challenges/{challengeId}/enroll")
    public UserChallengeEnrollmentResponseDto enroll(@PathVariable Long userId, @PathVariable Long challengeId) {
        return enrollmentService.enroll(userId, challengeId);
    }

    @PatchMapping("/{userId}/challenges/{challengeId}/status")
    public UserChallengeEnrollmentResponseDto updateStatus(
            @PathVariable Long userId,
            @PathVariable Long challengeId,
            @RequestBody UpdateEnrollmentStatusRequestDto request) {
        return enrollmentService.updateStatus(userId, challengeId, request.getStatus());
    }

    @GetMapping("/{userId}/challenges")
    public List<UserChallengeEnrollmentResponseDto> getUserEnrollments(@PathVariable Long userId) {
        return enrollmentService.getEnrollmentsForUser(userId);
    }

    @DeleteMapping("/{userId}/challenges/{challengeId}")
    public void unenroll(@PathVariable Long userId, @PathVariable Long challengeId) {
        enrollmentService.unenroll(userId, challengeId);
    }
}


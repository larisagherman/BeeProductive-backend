package com.beeproductive.backend.dto;

import com.beeproductive.backend.entity.ChallengeEnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChallengeEnrollmentResponseDto {
    private Long userId;
    private Long challengeId;
    private ChallengeEnrollmentStatus status;
    private Instant enrolledAt;
    private Instant updatedAt;
}


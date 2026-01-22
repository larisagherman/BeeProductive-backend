package com.beeproductive.backend.dto;

import com.beeproductive.backend.entity.ChallengeEnrollmentStatus;
import com.beeproductive.backend.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChallengeEnrollmentDto {
    private Long challengeId;
    private String challengeName;
    private String challengeDescription;
    private ChallengeType challengeType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int rewardBees;
    private ChallengeEnrollmentStatus status;
    private Instant enrolledAt;
    private Instant updatedAt;

    // Type-specific fields (only populated based on type)
    private Integer reductionPercentage;      // For SCREEN_TIME_REDUCTION
    private Set<String> blockedApps;          // For APP_BLOCKING
    private Integer maxDailyMinutes;          // For DAILY_LIMIT
}


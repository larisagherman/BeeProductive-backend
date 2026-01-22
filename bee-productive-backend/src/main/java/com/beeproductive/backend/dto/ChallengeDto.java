package com.beeproductive.backend.dto;

import com.beeproductive.backend.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeDto {
    private Long id;
    private String name;
    private String description;
    private ChallengeType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private int rewardBees;

    // Type-specific fields (only populated based on type)
    private Integer reductionPercentage;      // For SCREEN_TIME_REDUCTION
    private Set<String> blockedApps;          // For APP_BLOCKING
    private Integer maxDailyMinutes;          // For DAILY_LIMIT
}


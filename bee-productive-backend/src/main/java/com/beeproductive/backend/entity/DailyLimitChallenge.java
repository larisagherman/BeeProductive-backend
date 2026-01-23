package com.beeproductive.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_limit_challenge")
@DiscriminatorValue("DAILY_LIMIT")
public class DailyLimitChallenge extends Challenge {

    @Column(name = "max_daily_minutes", nullable = false)
    private int maxDailyMinutes; // Maximum screen time allowed per day in minutes
}


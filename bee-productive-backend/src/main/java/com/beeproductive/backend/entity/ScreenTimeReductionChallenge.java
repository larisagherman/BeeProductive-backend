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
@Table(name = "screen_time_reduction_challenge")
@DiscriminatorValue("SCREEN_TIME_REDUCTION")
public class ScreenTimeReductionChallenge extends Challenge {

    @Column(name = "reduction_percentage", nullable = false)
    private int reductionPercentage; // e.g., 20 means reduce by 20%

    @Column(name = "baseline_screen_time")
    private Long baselineScreenTime; // Baseline screen time in minutes (optional, can be calculated)
}

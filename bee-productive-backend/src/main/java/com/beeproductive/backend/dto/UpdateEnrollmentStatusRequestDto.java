package com.beeproductive.backend.dto;

import com.beeproductive.backend.entity.ChallengeEnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEnrollmentStatusRequestDto {
    private ChallengeEnrollmentStatus status;
}


package com.beeproductive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinChallengeRequestDto {
    private Long challengeId;
    private String userUid;
}

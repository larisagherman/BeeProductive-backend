package com.beeproductive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardResponseDto {
    private String groupName;
    private String groupCode;
    private List<LeaderboardUserDto> leaderboard;
}


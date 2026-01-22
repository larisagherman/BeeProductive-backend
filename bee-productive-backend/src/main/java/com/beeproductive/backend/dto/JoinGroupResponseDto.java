package com.beeproductive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinGroupResponseDto {
    private String message;
    private String groupName;
    private String groupCode;
}


package com.beeproductive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinGroupRequestDto {
    private String groupCode;
    private String userUid;
}

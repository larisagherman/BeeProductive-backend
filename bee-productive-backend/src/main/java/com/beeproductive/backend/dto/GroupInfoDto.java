package com.beeproductive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfoDto {
    private Long id;
    private String name;
    private String code;
    private String adminName;
    private int memberCount;
}


package com.beeproductive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupDto {
    private Long id;
    private String name;
    private String code;
    private boolean isAdmin;
    private int memberCount;
}


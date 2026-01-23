package com.beeproductive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private int numberOfBees;
    private int numberOfGroups;
}



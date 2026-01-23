package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.UserInfoDto;
import com.beeproductive.backend.security.FirebaseUserPrincipal;
import com.beeproductive.backend.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserInfoController {
    private UserInfoService userInfoService;

    @GetMapping("/info")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal FirebaseUserPrincipal principal) {
        return userInfoService.getUserInfo(principal.getUid());
    }
}


package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.*;
import com.beeproductive.backend.security.FirebaseUserPrincipal;
import com.beeproductive.backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;

    @PostMapping("/create")
    public GroupResponseDto createGroup(
            @RequestBody CreateGroupRequestDto groupRequestDto,
            @AuthenticationPrincipal FirebaseUserPrincipal principal) {
        // Set the authenticated user's UID in the DTO
        groupRequestDto.setUserUid(principal.getUid());
        return groupService.createGroup(groupRequestDto);
    }

    @PostMapping("/join")
    public JoinGroupResponseDto joinGroup(
            @RequestBody JoinGroupRequestDto joinGroupRequestDto,
            @AuthenticationPrincipal FirebaseUserPrincipal principal) {
        // Set the authenticated user's UID in the DTO
        joinGroupRequestDto.setUserUid(principal.getUid());
        return groupService.joinGroup(joinGroupRequestDto);
    }

    @GetMapping("/leaderboard/{groupCode}")
    public LeaderboardResponseDto getLeaderboard(@PathVariable String groupCode) {
        return groupService.getLeaderboard(groupCode);
    }
}

package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.MemberDetailsRequestDto;
import com.beeproductive.backend.dto.MemberDetailsResponseDto;
import com.beeproductive.backend.entity.MemberDetails;
import com.beeproductive.backend.entity.MemberKey;
import com.beeproductive.backend.service.MemberDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController("/member-details")
public class MemberDetailsController {
    private final MemberDetailsService memberDetailsService;
    @GetMapping("/groups-users")
    public List<MemberDetailsResponseDto> getAllMemberDetails() {
        return memberDetailsService.getAllMemberDetails();
    }
    @PostMapping("/groups/{groupId}/members/{userId}")
    public void addMember(@PathVariable Long userId, @PathVariable Long groupId) {
        memberDetailsService.addMember(userId, groupId);
    }
    @GetMapping("/groups/{groupId}/members/{userId}")
    public MemberDetailsResponseDto findMember(@PathVariable Long userId, @PathVariable Long groupId) {
        MemberKey memberKey = new MemberKey(userId, groupId);
        return memberDetailsService.getMemberDetailsByMemberKey(memberKey);
    }

    @DeleteMapping("/groups/{groupId}/members/{userId}")
    public void deleteMember(@PathVariable Long userId, @PathVariable Long groupId) {
        memberDetailsService.removeMember(userId,groupId);
    }
}

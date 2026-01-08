package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.GroupRequestDto;
import com.beeproductive.backend.dto.GroupResponseDto;
import com.beeproductive.backend.service.GroupService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;
    @PostMapping
    public void createGroup(@RequestBody GroupRequestDto groupRequestDto) {
        groupService.createGroup(groupRequestDto);
    }
    @GetMapping
    public List<GroupResponseDto> getGroups() {

        return groupService.getAllGroups();
    }
    @GetMapping("/{id}")
    public GroupResponseDto getGroupById(@PathVariable("id") Long id) {
        return groupService.getGroupById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteGroupById(@PathVariable("id") Long id) {
        groupService.deleteGroupById(id);
    }

    @PutMapping("/update-group-details/{id}")
    public void updateGroupById(@PathVariable("id") Long id, @RequestBody GroupRequestDto groupRequestDto) {
        groupService.updateGroupDetailsByGroupId(id, groupRequestDto);
    }

    @PutMapping("/{id}/add-group-members")
    public void updateMembersGroupById(@PathVariable("id") Long id, @RequestBody GroupRequestDto groupRequestDto) {
        groupService.updateGroupMembersByGroupId(id, groupRequestDto);
    }
}

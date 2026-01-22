package com.beeproductive.backend.service;

import com.beeproductive.backend.dto.CreateGroupRequestDto;
import com.beeproductive.backend.dto.GroupResponseDto;
import com.beeproductive.backend.dto.JoinGroupRequestDto;
import com.beeproductive.backend.dto.JoinGroupResponseDto;
import com.beeproductive.backend.dto.LeaderboardResponseDto;
import com.beeproductive.backend.dto.LeaderboardUserDto;
import com.beeproductive.backend.entity.Group;
import com.beeproductive.backend.entity.User;
import com.beeproductive.backend.exception.AlreadyMemberException;
import com.beeproductive.backend.exception.GroupNotFoundException;
import com.beeproductive.backend.repository.GroupRepository;
import com.beeproductive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public GroupResponseDto createGroup(CreateGroupRequestDto groupRequestDto) {
        // Find or create user
        User user = userRepository.findByFireBaseId(groupRequestDto.getUserUid())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setFireBaseId(groupRequestDto.getUserUid());
                    newUser.setName("User"); // Default name, can be updated later
                    newUser.setNumberOfBees(0);
                    return userRepository.save(newUser);
                });

        // Generate unique code for the group
        String groupCode = generateUniqueGroupCode();

        // Create and save group
        Group group = new Group();
        group.setName(groupRequestDto.getName());
        group.setCode(groupCode);
        group.setUserAdmin(user);

        // Add admin user to the group's users
        group.getUsers().add(user);

        Group savedGroup = groupRepository.save(group);

        return new GroupResponseDto(savedGroup.getName(), savedGroup.getCode());
    }

    private String generateUniqueGroupCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_CHARACTERS.charAt(random.nextInt(CODE_CHARACTERS.length())));
            }
            code = sb.toString();
        } while (groupRepository.existsByCode(code));
        return code;
    }

    @Transactional
    public JoinGroupResponseDto joinGroup(JoinGroupRequestDto joinGroupRequestDto) {
        // Validate group code is not null or empty
        if (joinGroupRequestDto.getGroupCode() == null || joinGroupRequestDto.getGroupCode().trim().isEmpty()) {
            throw new GroupNotFoundException("Group code cannot be empty");
        }

        // Normalize the group code (trim and uppercase)
        String normalizedCode = joinGroupRequestDto.getGroupCode().trim().toUpperCase();

        // Check if group exists
        Optional<Group> groupOptional = groupRepository.findByCode(normalizedCode);
        if (groupOptional.isEmpty()) {
            throw new GroupNotFoundException("Group with code '" + normalizedCode + "' does not exist");
        }

        Group group = groupOptional.get();

        // Find or create user
        User user = userRepository.findByFireBaseId(joinGroupRequestDto.getUserUid())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setFireBaseId(joinGroupRequestDto.getUserUid());
                    newUser.setName("User"); // Default name, can be updated later
                    newUser.setNumberOfBees(0);
                    return userRepository.save(newUser);
                });

        // Check if user is already in the group
        if (group.getUsers().contains(user)) {
            throw new AlreadyMemberException("You are already a member of this group");
        }

        // Add user to group
        group.getUsers().add(user);
        groupRepository.save(group);

        return new JoinGroupResponseDto(
                "Successfully joined the group",
                group.getName(),
                group.getCode()
        );
    }

    @Transactional(readOnly = true)
    public LeaderboardResponseDto getLeaderboard(String groupCode) {
        // Validate group code is not null or empty
        if (groupCode == null || groupCode.trim().isEmpty()) {
            throw new GroupNotFoundException("Group code cannot be empty");
        }

        // Normalize the group code (trim and uppercase)
        String normalizedCode = groupCode.trim().toUpperCase();

        // Check if group exists
        Optional<Group> groupOptional = groupRepository.findByCode(normalizedCode);
        if (groupOptional.isEmpty()) {
            throw new GroupNotFoundException("Group with code '" + normalizedCode + "' does not exist");
        }

        Group group = groupOptional.get();

        // Get all users in the group and sort by number of bees (descending)
        List<User> users = new ArrayList<>(group.getUsers());
        users.sort(Comparator.comparingInt(User::getNumberOfBees).reversed());

        // Create leaderboard with ranks
        List<LeaderboardUserDto> leaderboard = new ArrayList<>();
        int rank = 1;
        int previousBees = -1;
        int actualRank = 1;

        for (User user : users) {
            if (user.getNumberOfBees() != previousBees) {
                rank = actualRank;
            }
            leaderboard.add(new LeaderboardUserDto(
                    user.getName(),
                    user.getNumberOfBees(),
                    rank
            ));
            previousBees = user.getNumberOfBees();
            actualRank++;
        }

        return new LeaderboardResponseDto(
                group.getName(),
                group.getCode(),
                leaderboard
        );
    }
}




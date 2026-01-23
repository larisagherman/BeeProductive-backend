package com.beeproductive.backend.service;

import com.beeproductive.backend.dto.UserInfoDto;
import com.beeproductive.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserInfoService {
    private UserRepository userRepository;

    public UserInfoDto getUserInfo(String userUid) {
        var user = userRepository.findByFireBaseId(userUid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int numberOfBees = user.getNumberOfBees();
        int numberOfGroups = user.getGroups().size();

        return new UserInfoDto(numberOfBees, numberOfGroups);
    }
}


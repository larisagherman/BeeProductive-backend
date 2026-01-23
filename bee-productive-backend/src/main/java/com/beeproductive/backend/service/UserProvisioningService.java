package com.beeproductive.backend.service;

import com.beeproductive.backend.entity.User;
import com.beeproductive.backend.repository.UserRepository;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserProvisioningService {

    private final UserRepository userRepository;

    public UserProvisioningService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User provisionUser(FirebaseToken firebaseToken) {
        String firebaseId = firebaseToken.getUid();

        // Try to find existing user
        Optional<User> userOptional = userRepository.findByFireBaseId(firebaseId);

        User user = userOptional.orElse(null);
        if (user == null) {
            user = new User();
            user.setFireBaseId(firebaseId);
            user.setName(firebaseToken.getName() != null ? firebaseToken.getName() : firebaseToken.getEmail());
            user.setNumberOfBees(0);
            user = userRepository.save(user);
        }

        return user;
    }
}

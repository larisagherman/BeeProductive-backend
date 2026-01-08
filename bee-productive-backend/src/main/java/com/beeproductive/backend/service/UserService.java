package com.beeproductive.backend.service;

import com.beeproductive.backend.dto.UserResponseDto;
import com.beeproductive.backend.entity.User;
import com.beeproductive.backend.mapper.UserMapper;
import com.beeproductive.backend.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public void login(String firebaseId,String email) {
        //this is just a mok login because we do not have the firebase login just yet
        User user=userRepository.findByFireBaseId(firebaseId);
        if(user==null){
            register(firebaseId, email);
        }

    }
    public void register(String firebaseUId, String email) {
        User user = new User();
        user.setFireBaseId(firebaseUId);
        user.setName(email.split("@")[0]);
        user.setNumberOfBees(10);
        userRepository.save(user);
    }
    public List<UserResponseDto> getAllUsers() {
        List<User> users=userRepository.findAll();
        return userMapper.usersToDtoResponses(users);
    }
    public UserResponseDto getUserById(Long id) {
        User user=userRepository.findById(id).orElseThrow(()-> new RuntimeException("User was not found"));
        return userMapper.userToDtoResponse(user);
    }
    public void deleteUser(Long id) {
        User user=userRepository.findById(id).orElseThrow(()-> new RuntimeException("User was not found"));
        userRepository.delete(user);
    }
}

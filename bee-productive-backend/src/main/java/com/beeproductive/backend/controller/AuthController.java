package com.beeproductive.backend.controller;

import com.beeproductive.backend.dto.UserResponseDto;
import com.beeproductive.backend.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody String firebaseId,String email){

        userService.login(firebaseId, email);
    }

}

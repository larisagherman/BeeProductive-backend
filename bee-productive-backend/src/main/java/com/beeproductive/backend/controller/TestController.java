package com.beeproductive.backend.controller;

import com.beeproductive.backend.security.FirebaseUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/secure")
    public String secure(Authentication auth) {
        Object principal = auth.getPrincipal();

        if (principal instanceof FirebaseUserPrincipal) {
            FirebaseUserPrincipal firebaseUser = (FirebaseUserPrincipal) principal;
            String realName = firebaseUser.getName();

            if (realName != null && !realName.isEmpty()) {
                return "Hello " + realName + "!";
            } else {
                // Fallback to email if name is not available
                String email = firebaseUser.getEmail();
                return "Hello " + (email != null ? email : firebaseUser.getUid()) + "!";
            }
        } else if (principal instanceof UserDetails) {
            return "Hello user " + ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return "Hello user " + (String) principal;
        } else {
            return "Hello user (Unknown)";
        }
    }
}

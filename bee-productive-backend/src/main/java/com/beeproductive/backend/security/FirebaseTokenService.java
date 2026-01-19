package com.beeproductive.backend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseTokenService {

    public FirebaseToken verify(String token)
            throws FirebaseAuthException {
        return FirebaseAuth.getInstance()
                .verifyIdToken(token);
    }
}

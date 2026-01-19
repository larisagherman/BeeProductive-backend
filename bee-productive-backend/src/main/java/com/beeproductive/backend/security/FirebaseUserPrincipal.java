package com.beeproductive.backend.security;

import com.google.firebase.auth.FirebaseToken;

public class FirebaseUserPrincipal {
    private final FirebaseToken token;
    private final String uid;

    public FirebaseUserPrincipal(FirebaseToken token) {
        this.token = token;
        this.uid = token.getUid();
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return token.getName();
    }

    public String getEmail() {
        return token.getEmail();
    }

    public FirebaseToken getToken() {
        return token;
    }

    @Override
    public String toString() {
        return uid;
    }
}

package com.beeproductive.backend.security;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class FirebaseAuthenticationFilter
        extends OncePerRequestFilter {

    private final FirebaseTokenService tokenService;

    public FirebaseAuthenticationFilter(
            FirebaseTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            FirebaseToken firebaseToken =
                    tokenService.verify(token);

            // Optional: Google-only check
            Map<String, Object> firebase =
                    (Map<String, Object>) firebaseToken
                            .getClaims()
                            .get("firebase");

            String provider =
                    (String) firebase.get("sign_in_provider");

            if (!"google.com".equals(provider)) {
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            new FirebaseUserPrincipal(firebaseToken),
                            null,
                            List.of()
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(auth);

        } catch (FirebaseAuthException e) {
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

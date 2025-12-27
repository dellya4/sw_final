package com.example.demo.security;

import com.example.demo.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static User getCurrentUser() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth.getPrincipal() instanceof String) {

            throw new RuntimeException("User is not authenticated");
        }

        CustomUserDetails details =
                (CustomUserDetails) auth.getPrincipal();

        return details.getUser();
    }
}


package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/api/admin/users/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/admin/wishes/**")
                        .hasAnyRole("ADMIN", "MODERATOR")


                        .requestMatchers("/api/users/me/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/wishes/published/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/wishes/*")
                        .permitAll()
                        .requestMatchers("/api/wishes/**")
                        .authenticated()

                        .requestMatchers("/api/groups/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/categories/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/bookings/**")
                        .authenticated()

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}


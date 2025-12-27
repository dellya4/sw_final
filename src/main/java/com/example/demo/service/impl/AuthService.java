package com.example.demo.service.impl;

import com.example.demo.dto.request.AuthRequest;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.UserResponseDto;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    UserResponseDto register(UserRequestDto dto);
}

